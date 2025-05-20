package com.food.ordering.system.order.domain.common.event.publisher;

import com.food.ordering.system.order.domain.common.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(final T domainEvent);

}
