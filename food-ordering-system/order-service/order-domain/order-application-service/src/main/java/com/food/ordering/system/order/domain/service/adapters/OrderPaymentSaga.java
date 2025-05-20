package com.food.ordering.system.order.domain.service.adapters;

import com.food.ordering.system.order.domain.common.event.EmptyEvent;
import com.food.ordering.system.order.domain.service.dto.message.PaymentResponse;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.event.OrderPaidEvent;
import com.food.ordering.system.order.domain.service.ports.output.message.publisher.restaurantapproval.OrderPayedRequestRestaurantMessagePublisher;
import com.food.ordering.system.order.domain.service.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.domain.service.service.OrderDomainService;
import com.food.ordering.system.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final OrderPayedRequestRestaurantMessagePublisher restaurantMessagePublisher;

    public OrderPaymentSaga(final OrderDomainService orderDomainService,
                            final OrderRepository orderRepository,
                            final OrderPayedRequestRestaurantMessagePublisher restaurantMessagePublisher) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.restaurantMessagePublisher = restaurantMessagePublisher;
    }

    @Override
    public OrderPaidEvent process(final PaymentResponse event) {
        log.info("Order Payment completing for orderId = {}", event.getOrderId());
        final Order order = orderRepository.findById(event.getOrderId());
        final OrderPaidEvent payedOrder = orderDomainService.payOrder(order, restaurantMessagePublisher);
        orderRepository.createOrder(order);
        log.info("Order with orderId = {} is payed!", event.getOrderId());
        return payedOrder;
    }

    @Override
    public EmptyEvent rollback(final PaymentResponse event) {
        log.info("Order Payment canceling for orderId = {}", event.getOrderId());
        final Order order = orderRepository.findById(event.getOrderId());
        orderDomainService.cancelOrder(order, event.getFailureMessages());
        orderRepository.createOrder(order);
        log.info("Order with orderId = {} is canceled!", event.getOrderId());
        return EmptyEvent.EMPTY_EVENT;
    }
}
