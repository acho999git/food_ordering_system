package com.food.ordering.system.order.domain.common.valueobject;

import java.util.UUID;

public class ProductId extends BaseId<UUID>{
    public ProductId(UUID value) {
        super(value);
    }
}
