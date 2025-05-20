package com.food.ordering.system.payment.domain.core.event;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;

import static java.util.Collections.emptyList;

public class PaymentCancelledEvent extends PaymentEvent{

    private final DomainEventPublisher<PaymentCancelledEvent> paymentEventPublisher;

    public PaymentCancelledEvent(final Payment payment, final ZonedDateTime createdAt,
                                 final DomainEventPublisher<PaymentCancelledEvent> paymentEventPublisher) {
        super(payment, createdAt, emptyList());
        this.paymentEventPublisher = paymentEventPublisher;
    }

    @Override
    public void fire() {

    }
}
