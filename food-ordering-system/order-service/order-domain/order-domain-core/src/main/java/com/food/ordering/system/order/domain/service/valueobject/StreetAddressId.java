package com.food.ordering.system.order.domain.service.valueobject;

import com.food.ordering.system.order.domain.common.valueobject.BaseId;

import java.util.UUID;

public class StreetAddressId extends BaseId<UUID> {
    public StreetAddressId(UUID value) {
        super(value);
    }
}
