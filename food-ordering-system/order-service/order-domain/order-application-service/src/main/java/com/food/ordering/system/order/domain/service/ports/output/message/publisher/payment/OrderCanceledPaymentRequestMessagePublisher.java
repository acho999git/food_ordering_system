package com.food.ordering.system.order.domain.service.ports.output.message.publisher.payment;

import com.food.ordering.system.order.domain.service.event.OrderCancelledEvent;
import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;

//Output port
public interface OrderCanceledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
