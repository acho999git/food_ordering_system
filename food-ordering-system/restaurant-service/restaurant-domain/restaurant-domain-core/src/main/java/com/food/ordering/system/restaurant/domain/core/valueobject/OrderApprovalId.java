package com.food.ordering.system.restaurant.domain.core.valueobject;

import com.food.ordering.system.order.domain.common.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
    public OrderApprovalId(final UUID value) {
        super(value);
    }
}
