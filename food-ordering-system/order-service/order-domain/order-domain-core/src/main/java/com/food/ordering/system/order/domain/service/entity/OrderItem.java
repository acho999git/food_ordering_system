package com.food.ordering.system.order.domain.service.entity;

import com.food.ordering.system.order.domain.common.entity.BaseEntity;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.order.domain.service.valueobject.OrderItemId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderItem extends BaseEntity<OrderItemId> {

    private OrderId orderId;
    private final Product product;
    private final Money price;
    private final Money subTotal;
    private final Integer quantity;

    public boolean isPriceValid() {
        return price.isGreaterThanZero()
                && product.getPrice().equals(price)
                && subTotal.equals(price.multiply(quantity));
    }

    void initializeOrderItem(final OrderId orderId, final OrderItemId orderItem) {
        this.orderId = orderId;
        setId(orderItem);
    }
}
