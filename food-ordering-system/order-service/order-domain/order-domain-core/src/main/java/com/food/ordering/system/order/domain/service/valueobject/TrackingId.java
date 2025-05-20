package com.food.ordering.system.order.domain.service.valueobject;

import com.food.ordering.system.order.domain.common.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID value) {
        super(value);
    }
}
