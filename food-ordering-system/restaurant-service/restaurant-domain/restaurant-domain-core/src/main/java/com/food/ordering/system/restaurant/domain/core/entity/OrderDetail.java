package com.food.ordering.system.restaurant.domain.core.entity;

import com.food.ordering.system.order.domain.common.entity.BaseEntity;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.order.domain.common.valueobject.OrderStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderDetail extends BaseEntity<OrderId> {

    private OrderStatus orderStatus;
    private Money amount;
    private List<Product> products;

    private OrderDetail (final Builder builder) {
        setId(builder.orderId);
        this.orderStatus = builder.orderStatus;
        this.amount = builder.amount;
        this.products = builder.products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        public OrderId orderId;
        public OrderStatus orderStatus;
        public Money amount;
        public List<Product> products;

        public Builder setOrderStatus(final OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder setOrderDetailId(final OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setAmount(final Money amount) {
            this.amount = amount;
            return this;
        }

        public Builder setProducts(final List<Product> products) {
            this.products = products;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(this);
        }
    }
}
