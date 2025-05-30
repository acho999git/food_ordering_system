package com.food.ordering.system.order.domain.common.event;

public class EmptyEvent implements DomainEvent<Void> {

    //singleton object
    public static final EmptyEvent EMPTY_EVENT = new EmptyEvent();

    private EmptyEvent() {}

    @Override
    public void fire() {

    }
}
