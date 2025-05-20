package com.food.ordering.system.restaurant.domain.core.entity;

import com.food.ordering.system.order.domain.common.entity.BaseEntity;
import com.food.ordering.system.order.domain.common.valueobject.OrderApprovalStatus;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.order.domain.common.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.domain.core.valueobject.OrderApprovalId;
import lombok.Getter;

@Getter
public class OrderApproval extends BaseEntity<OrderApprovalId> {

    private final OrderId orderId;
    private final RestaurantId restaurantId;
    private final OrderApprovalStatus orderApprovalStatus;

    private OrderApproval(final Builder builder) {
        setId(builder.orderApprovalId);
        this.orderId = builder.orderId;
        this.restaurantId = builder.restaurantId;
        this.orderApprovalStatus = builder.orderApprovalStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        public OrderApprovalId orderApprovalId;
        public OrderId orderId;
        public RestaurantId restaurantId;
        public OrderApprovalStatus orderApprovalStatus;

        public Builder setOrderId(final OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setOrderApprovalId(final OrderApprovalId orderApprovalId) {
            this.orderApprovalId = orderApprovalId;
            return this;
        }

        public Builder setRestaurantId(final RestaurantId restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public Builder setOrderApprovalStatus(final OrderApprovalStatus orderApprovalStatus) {
            this.orderApprovalStatus = orderApprovalStatus;
            return this;
        }

        public OrderApproval build() {
            return new OrderApproval(this);
        }
    }
}
