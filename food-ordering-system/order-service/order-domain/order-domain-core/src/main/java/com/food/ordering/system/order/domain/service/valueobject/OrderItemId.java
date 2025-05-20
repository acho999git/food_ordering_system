package com.food.ordering.system.order.domain.service.valueobject;


import com.food.ordering.system.order.domain.common.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(final Long value) {
        super(value);
    }
}
