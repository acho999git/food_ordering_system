package com.food.ordering.system.saga;

import com.food.ordering.system.order.domain.common.event.DomainEvent;

public interface SagaStep<T,S extends DomainEvent, U extends DomainEvent> {
    S process(T event);
    U rollback(T event);
}
