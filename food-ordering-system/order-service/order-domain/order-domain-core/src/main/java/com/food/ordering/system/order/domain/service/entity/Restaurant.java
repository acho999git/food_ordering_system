package com.food.ordering.system.order.domain.service.entity;

import com.food.ordering.system.order.domain.common.entity.AggregateRoot;
import com.food.ordering.system.order.domain.common.valueobject.RestaurantId;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

    private final List<Product> products;
    private final boolean isActive;

    public Restaurant(final List<Product> products, final boolean isActive, final UUID id) {
        this.products = products;
        this.isActive = isActive;
        super.setId(new RestaurantId(id));
    }
}
