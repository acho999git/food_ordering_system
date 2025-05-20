package com.food.ordering.system.restaurant.domain.core.entity;

import com.food.ordering.system.order.domain.common.entity.AggregateRoot;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.order.domain.common.valueobject.OrderApprovalStatus;
import com.food.ordering.system.order.domain.common.valueobject.OrderStatus;
import com.food.ordering.system.order.domain.common.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.domain.core.valueobject.OrderApprovalId;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

    private OrderDetail orderDetail;
    private boolean active;
    private OrderApproval orderApproval;

    private Restaurant (final Builder builder) {
        setId(builder.restaurantId);
        this.orderDetail = builder.orderDetail;
        this.active = builder.active;
        this.orderApproval = builder.orderApproval;
    }

    public void validateOrder(final List<String> failureMessages) {
        if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
            failureMessages.add("Payment is not completed for order: " + orderDetail.getId());
        }
        Money totalAmount = Money.builder()
                .amount(getAmount(failureMessages))
                .build();
        if (!totalAmount.equals(orderDetail.getAmount())) {
            failureMessages.add("Price total is not correct for order: " + orderDetail.getId());
        }
    }

    private BigDecimal getAmount(final List<String> failureMessages) {
        return orderDetail.getProducts().stream().map(product -> {
            if (!product.isAvailable()) {
                failureMessages.add("Product with id: " + product.getId().getValue()
                        + " is not available");
            }
            return product.getPrice().multiply(new BigDecimal(product.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void constructOrderApproval(final OrderApprovalStatus orderApprovalStatus) {
        this.orderApproval = OrderApproval.builder()
                .setOrderApprovalId(new OrderApprovalId(UUID.randomUUID()))
                .setRestaurantId(this.getId())
                .setOrderId(this.getOrderDetail().getId())
                .setOrderApprovalStatus(orderApprovalStatus)
                .build();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        public RestaurantId restaurantId;
        public OrderDetail orderDetail;
        public boolean active;
        public OrderApproval orderApproval;

        public Builder setOrderDetail(final OrderDetail orderDetail) {
            this.orderDetail = orderDetail;
            return this;
        }

        public Builder setActive(final boolean active) {
            this.active = active;
            return this;
        }

        public Builder setOrderApproval(final OrderApproval orderApproval) {
            this.orderApproval = orderApproval;
            return this;
        }

        public Builder setRestaurantId(final RestaurantId restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
