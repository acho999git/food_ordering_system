package com.food.ordering.system.order.domain.service.service;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.entity.Restaurant;
import com.food.ordering.system.order.domain.service.event.OrderCancelledEvent;
import com.food.ordering.system.order.domain.service.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.service.event.OrderPaidEvent;

import java.util.List;
//this is domain service which is being used in the order-application-service layer in order-domain
//and this service is accessible only from this order-application-service
public interface OrderDomainService {

    OrderCreatedEvent createOrder(final Order order, final Restaurant restaurant, final DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher);

    OrderCancelledEvent cancelOrderPayment(final Order order,
                                           final List<String> failureMessages,
                                           final DomainEventPublisher<OrderCancelledEvent> orderCanceledEventPublisher);

    OrderPaidEvent payOrder(final Order order,
                            final DomainEventPublisher<OrderPaidEvent> orderCreatedEventPublisher);

    void cancelOrder(final Order order, final List<String> failureMessages);

    void approveOrder(final Order order);

}
