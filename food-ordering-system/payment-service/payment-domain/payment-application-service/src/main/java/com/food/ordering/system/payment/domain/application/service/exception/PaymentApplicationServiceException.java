package com.food.ordering.system.payment.domain.application.service.exception;

import com.food.ordering.system.order.domain.common.exception.DomainException;

public class PaymentApplicationServiceException extends DomainException {

    public PaymentApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentApplicationServiceException(String message) {
        super(message);
    }
}
