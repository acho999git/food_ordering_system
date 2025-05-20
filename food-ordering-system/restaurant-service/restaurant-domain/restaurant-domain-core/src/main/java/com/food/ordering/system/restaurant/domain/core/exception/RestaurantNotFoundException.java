package com.food.ordering.system.restaurant.domain.core.exception;

import com.food.ordering.system.order.domain.common.exception.DomainException;

public class RestaurantNotFoundException extends DomainException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
