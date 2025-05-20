package com.food.ordering.system.order.domain.service.exception;

import com.food.ordering.system.order.domain.common.exception.DomainException;

public class OrderDomainException extends DomainException {

    public OrderDomainException(String message) {
        super(message);
    }

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
