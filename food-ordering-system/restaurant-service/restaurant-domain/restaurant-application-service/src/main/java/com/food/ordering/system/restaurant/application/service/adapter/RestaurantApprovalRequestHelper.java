package com.food.ordering.system.restaurant.application.service.adapter;

import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.restaurant.application.service.dto.RestaurantApprovalRequestDto;
import com.food.ordering.system.restaurant.application.service.mapper.RestaurantDataMapper;
import com.food.ordering.system.restaurant.application.service.port.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.application.service.port.output.message.publisher.OrderRejectedMessagePublisher;
import com.food.ordering.system.restaurant.application.service.port.output.repository.OrderApprovalRepository;
import com.food.ordering.system.restaurant.application.service.port.output.repository.RestaurantRepository;
import com.food.ordering.system.restaurant.domain.core.entity.Restaurant;
import com.food.ordering.system.restaurant.domain.core.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.domain.core.exception.RestaurantNotFoundException;
import com.food.ordering.system.restaurant.domain.core.service.RestaurantDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RestaurantApprovalRequestHelper {

    private final OrderApprovalRepository approvalRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper dataMapper;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

    public RestaurantApprovalRequestHelper(final OrderApprovalRepository approvalRepository,
                                           final RestaurantRepository restaurantRepository,
                                           final RestaurantDomainService restaurantDomainService,
                                           final RestaurantDataMapper dataMapper,
                                           final OrderApprovedMessagePublisher orderApprovedMessagePublisher,
                                           final OrderRejectedMessagePublisher orderRejectedMessagePublisher) {
        this.approvalRepository = approvalRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantDomainService = restaurantDomainService;
        this.dataMapper = dataMapper;
        this.orderApprovedMessagePublisher = orderApprovedMessagePublisher;
        this.orderRejectedMessagePublisher = orderRejectedMessagePublisher;
    }

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequestDto restaurantApprovalRequest) {
        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApprovalEvent orderApprovalEvent =
                restaurantDomainService.validateOrder(
                        restaurant,
                        failureMessages,
                        orderApprovedMessagePublisher,
                        orderRejectedMessagePublisher);
        approvalRepository.save(restaurant.getOrderApproval());
        return orderApprovalEvent;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequestDto restaurantApprovalRequest) {
        Restaurant restaurant = dataMapper
                .restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> restaurantResult = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantResult.isEmpty()) {
            log.error("Restaurant with id " + restaurant.getId().getValue() + " not found!");
            throw new RestaurantNotFoundException("Restaurant with id " + restaurant.getId().getValue() +
                    " not found!");
        }

        Restaurant restaurantEntity = restaurantResult.get();
        restaurant.setActive(restaurantEntity.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product ->
                restaurantEntity.getOrderDetail().getProducts().forEach(p -> {
                    if (p.getId().equals(product.getId())) {
                        product.updateWithNamePriceAndAvailable(p.getProductName(), p.getPrice(), p.isAvailable());
                    }
                }));
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));

        return restaurant;
    }
}
