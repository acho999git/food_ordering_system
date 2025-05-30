package com.food.ordering.system.order.domain.service.adapters;

import com.food.ordering.system.order.domain.common.event.EmptyEvent;
import com.food.ordering.system.order.domain.service.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.event.OrderCancelledEvent;
import com.food.ordering.system.order.domain.service.mapper.OrderDtoMapper;
import com.food.ordering.system.order.domain.service.ports.output.message.publisher.payment.OrderCanceledPaymentRequestMessagePublisher;
import com.food.ordering.system.order.domain.service.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.domain.service.service.OrderDomainService;
import com.food.ordering.system.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final OrderCanceledPaymentRequestMessagePublisher publisher;
    private final OrderDtoMapper orderDtoMapper;

    public OrderApprovalSaga(final OrderDomainService orderDomainService,
                             final OrderRepository orderRepository,
                             final OrderCanceledPaymentRequestMessagePublisher requestMessagePublisher, OrderDtoMapper orderDtoMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.publisher = requestMessagePublisher;
        this.orderDtoMapper = orderDtoMapper;
    }

    @Override
    @Transactional
    public EmptyEvent process(final RestaurantApprovalResponse event) {
        log.info("Order approval for orderId = {}", event.getOrderId());
        final Order order = orderRepository.findById(event.getOrderId());
        orderDomainService.approveOrder(order);
        orderRepository.createOrder(order);
        log.info("Order with orderId = {} is approved!", event.getOrderId());
        return EmptyEvent.EMPTY_EVENT;
    }

    @Override
    @Transactional
    public OrderCancelledEvent rollback(final RestaurantApprovalResponse event) {
        log.info("Canceling order with orderId = {}", event.getOrderId());
        final Order order = orderRepository.findById(event.getOrderId());
        orderDomainService.cancelOrder(order, event.getFailureMessages());
        final OrderCancelledEvent cancelledEvent = orderDomainService.cancelOrderPayment(order,
                event.getFailureMessages(),
                publisher);
        log.info("Order with orderId = {} is canceled!", event.getOrderId());
        return cancelledEvent;
    }
}
