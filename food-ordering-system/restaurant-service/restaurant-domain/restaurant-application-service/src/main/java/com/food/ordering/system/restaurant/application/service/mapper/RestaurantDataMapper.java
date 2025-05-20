package com.food.ordering.system.restaurant.application.service.mapper;

import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.order.domain.common.valueobject.OrderStatus;
import com.food.ordering.system.order.domain.common.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.application.service.dto.RestaurantApprovalRequestDto;
import com.food.ordering.system.restaurant.domain.core.entity.OrderDetail;
import com.food.ordering.system.restaurant.domain.core.entity.Product;
import com.food.ordering.system.restaurant.domain.core.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataMapper {

    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequestDto
                                                                    restaurantApprovalRequest) {
        return Restaurant.builder()
                .setRestaurantId(new RestaurantId(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
                .setOrderDetail(OrderDetail.builder()
                        .setOrderDetailId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                        .setProducts(restaurantApprovalRequest.getProducts().stream().map(
                                        product -> Product.builder()
                                                .setProductId(product.getId())
                                                .setQuantity(product.getQuantity())
                                                .build())
                                .collect(Collectors.toList()))
                        .setAmount(new Money(restaurantApprovalRequest.getPrice()))
                        .setOrderStatus(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                        .build())
                .build();
    }

}
