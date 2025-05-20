package com.food.ordering.system.order.domain.service.event;

import com.food.ordering.system.order.domain.common.event.DomainEvent;
import com.food.ordering.system.order.domain.service.entity.Order;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime timestamp;

    public OrderEvent(Order order, ZonedDateTime timestamp) {
        this.order = order;
        this.timestamp = timestamp;
    }
}
