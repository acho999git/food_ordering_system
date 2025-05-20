package com.food.ordering.system.order.domain.service.exception;

import com.food.ordering.system.order.domain.common.exception.DomainException;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
