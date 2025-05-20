package com.food.ordering.system.order.domain.service.ports.output.message.publisher.payment;

import com.food.ordering.system.order.domain.service.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;

//Output port
public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
