package com.food.ordering.system.order.domain.common.valueobject;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class BaseId<T> {

    public final T value;

    protected BaseId(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;//that means check if it is the same object with the same reference
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;//this is with wildcard because the type is generic
        return Objects.equals(value, baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
