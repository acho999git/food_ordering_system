package com.food.ordering.system.order.domain.service.adapters;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.domain.common.exception.DomainException;
import com.food.ordering.system.order.domain.service.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.domain.service.entity.Customer;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.entity.Restaurant;
import com.food.ordering.system.order.domain.service.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.service.mapper.OrderDtoMapper;
import com.food.ordering.system.order.domain.service.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.domain.service.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.domain.service.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.order.domain.service.service.OrderDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class CreateOrderHelper {

    private final OrderDomainService orderDomainService;// with this field we
    //connect order domain logic from order-domain-core with the outside world logic
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDtoMapper mapper;
    private final DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher;

    public CreateOrderHelper(final OrderDomainService orderDomainService,
                             final OrderRepository orderRepository,
                             final CustomerRepository customerRepository,
                             final RestaurantRepository restaurantRepository,
                             final OrderDtoMapper mapper,
                             final DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.mapper = mapper;
        this.orderCreatedEventPublisher = orderCreatedEventPublisher;
    }

    @Transactional//in order for this to work this class has been created because the method with the
    //transactional annotation have to be called from spring bean class that is why this class will be injected
    //in CreateOrderCommandHandler class and this method will be called from it
    public OrderCreatedEvent persistOrder(final CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        final Restaurant restaurant = checkRestaurant(createOrderCommand);
        final Order order = mapper.toOrder(createOrderCommand);
        saveOrder(order);
        log.info("Created order: {}", order);
        return this.orderDomainService.createOrder(order, restaurant, orderCreatedEventPublisher);
    }

    private Restaurant checkRestaurant(final CreateOrderCommand createOrderCommand) {
        final Restaurant restaurant = mapper.toRestaurant(createOrderCommand);
        final Optional<Restaurant> restaurantOpt = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantOpt.isEmpty()) {
            log.error("Restaurant {} not found", restaurant);
            throw new DomainException("Restaurant not found");
        }
        return restaurantOpt.get();
    }

    private void checkCustomer(final UUID customerId) {
        final Optional<Customer> customer = this.customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.error("Customer not found");
            throw new DomainException("Customer not found");
        }
    }

    private Order saveOrder(final Order order) {
        final Order savedOrder = this.orderRepository.createOrder(order);

        if (savedOrder == null) {
            log.error("Save order failed");
            return Order.builder().build();
        }

        log.info("Save order successful");
        return savedOrder;
    }


}
