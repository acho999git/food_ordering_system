package com.food.ordering.system.order.domain.common.valueobject;

import java.util.UUID;


public class OrderId extends BaseId<UUID>{
    public OrderId(UUID value) {
        super(value);
    }
}
