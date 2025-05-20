package com.food.ordering.system.order.domain.common.valueobject;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING, PAID, APPROVED, CANCELING, CANCELLED;
}
