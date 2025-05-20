package com.food.ordering.system.order.domain.service.entity;

import com.food.ordering.system.order.domain.common.entity.AggregateRoot;
import com.food.ordering.system.order.domain.common.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    public Customer() {}

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }

}
