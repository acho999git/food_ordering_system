package com.food.ordering.system.restaurant.domain.core.entity;

import com.food.ordering.system.order.domain.common.entity.BaseEntity;
import com.food.ordering.system.order.domain.common.valueobject.ProductId;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Product extends BaseEntity<ProductId> {

    private String productName;
    private BigDecimal price;
    private int quantity;
    private boolean isAvailable;

    private Product(final Builder builder) {
        setId(builder.productId);
        this.productName = builder.productName;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.isAvailable = builder.isAvailable;
    }

    public void updateWithNamePriceAndAvailable(final String productName,
                                                final BigDecimal price,
                                                final boolean isAvailable) {
        this.price = price;
        this.isAvailable = isAvailable;
        this.productName = productName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        public ProductId productId;
        public String productName;
        public BigDecimal price;
        public int quantity;
        public boolean isAvailable;

        public Builder setProductId(final ProductId productId) {
            this.productId = productId;
            return this;
        }

        public Builder setProductName(final String name) {
            this.productName = name;
            return this;
        }

        public Builder setPrice(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setQuantity(final int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setAvailable(final boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
