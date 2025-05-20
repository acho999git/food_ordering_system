package com.food.ordering.system.payment.domain.core.event;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent{

    private final DomainEventPublisher<PaymentFailedEvent> domainEventPublisher;

    public PaymentFailedEvent(final Payment payment,
                              final ZonedDateTime createdAt,
                              final List<String> failureMessages,
                              final DomainEventPublisher<PaymentFailedEvent> domainEventPublisher) {
        super(payment, createdAt, failureMessages);
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        domainEventPublisher.publish(this);
    }
}
