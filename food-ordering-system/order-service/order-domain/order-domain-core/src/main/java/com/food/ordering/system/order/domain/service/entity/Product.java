package com.food.ordering.system.order.domain.service.entity;

import com.food.ordering.system.order.domain.common.entity.BaseEntity;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.order.domain.common.valueobject.ProductId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Product extends BaseEntity<ProductId> {

    private String name;
    private Money price;

    public Product(final ProductId productId) {
        super.setId(productId);
    }

    public void updateProductWithConfirmedNameAndPrice(final String name, final Money price){
        this.name = name;
        this.price = price;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
