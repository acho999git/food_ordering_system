package com.food.ordering.system.payment.domain.core.event;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;

import static java.util.Collections.emptyList;

public class PaymentCompletedEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCompletedEvent> domainEventPublisher;

    public PaymentCompletedEvent(final Payment payment, final ZonedDateTime createdAt,
                                 final DomainEventPublisher<PaymentCompletedEvent>  domainEventPublisher) {
        super(payment, createdAt, emptyList());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {

    }
}
