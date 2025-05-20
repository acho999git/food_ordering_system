package com.food.ordering.system.order.domain.service.event;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.domain.service.entity.Order;
import lombok.Builder;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCreatedEvent> domainEventPublisher;

    @Builder
    public OrderCreatedEvent(final Order order,
                             final ZonedDateTime timestamp,
                             final DomainEventPublisher<OrderCreatedEvent> domainEventPublisher) {
        super(order, timestamp);
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        domainEventPublisher.publish(this);
    }
}
