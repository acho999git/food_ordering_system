package com.food.ordering.system.order.domain.common.event;

public class EmptyEvent implements DomainEvent<Void> {

    public static final EmptyEvent EMPTY_EVENT = new EmptyEvent();

    private EmptyEvent() {}

    @Override
    public void fire() {

    }
}
