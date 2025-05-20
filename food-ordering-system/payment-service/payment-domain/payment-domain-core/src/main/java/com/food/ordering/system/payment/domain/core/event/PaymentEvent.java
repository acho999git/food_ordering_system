package com.food.ordering.system.payment.domain.core.event;

import com.food.ordering.system.order.domain.common.event.DomainEvent;
import com.food.ordering.system.payment.domain.core.entity.Payment;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public abstract class PaymentEvent implements DomainEvent<Payment> {

    private final Payment payment;
    private final ZonedDateTime createdAt;
    private final List<String> failureMessages;

    public PaymentEvent(final Payment payment,
                        final ZonedDateTime createdAt,
                        final List<String> failureMessages) {
        this.payment = payment;
        this.createdAt = createdAt;
        this.failureMessages = failureMessages;
    }

}
