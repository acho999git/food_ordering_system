package com.food.ordering.system.order.domain.common.event;

public interface DomainEvent<T> {// this will be marker interface which will mark
    // the event object with the entity object type <T> which will fire this event
    void fire();
}
