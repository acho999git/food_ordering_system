package com.food.ordering.system.restaurant.domain.core.event;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.domain.common.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.domain.core.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher;

    public OrderRejectedEvent(final OrderApproval orderApproval,
                              final RestaurantId restaurantId,
                              final List<String> failureMessages,
                              final ZonedDateTime createdAt,
                              final DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.orderRejectedEventDomainEventPublisher = orderRejectedEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderRejectedEventDomainEventPublisher.publish(this);
    }
}
