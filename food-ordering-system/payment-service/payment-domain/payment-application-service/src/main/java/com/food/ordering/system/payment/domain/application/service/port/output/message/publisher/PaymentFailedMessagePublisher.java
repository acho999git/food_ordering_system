package com.food.ordering.system.payment.domain.application.service.port.output.message.publisher;


import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.domain.core.event.PaymentFailedEvent;

//output port which will be implemented in payment-messaging adapters
public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFailedEvent> {
}
