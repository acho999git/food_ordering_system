package com.food.ordering.system.payment.domain.core.valueobject;

import com.food.ordering.system.order.domain.common.valueobject.BaseId;

import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {
    public CreditEntryId(final UUID value) {
        super(value);
    }
}