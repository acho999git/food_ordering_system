package com.food.ordering.system.payment.domain.core.exception;

import com.food.ordering.system.order.domain.common.exception.DomainException;

public class PaymentDomainException extends DomainException {

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentDomainException(String message) {
        super(message);
    }
}
