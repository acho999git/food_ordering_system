package com.food.ordering.system.order.domain.service.entity;

import com.food.ordering.system.order.domain.common.entity.AggregateRoot;
import com.food.ordering.system.order.domain.common.valueobject.*;
import com.food.ordering.system.order.domain.service.exception.OrderDomainException;
import com.food.ordering.system.order.domain.service.valueobject.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

@Builder
@Getter
public class Order extends AggregateRoot<OrderId> {

    public static final String STATE_FOR_S_OPERATION = "Order is not in the correct state for %s operation!";

    private final OrderId orderId;
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final List<OrderItem> items;
    private final Money price;
    private final StreetAddress deliveryAddress;
    private List<String> failureMessages;// all this fields are final because the object is immutable
    // and they can be changed only with new object creation or local methods which will change their state

    private OrderStatus orderStatus;
    private TrackingId trackingId;

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException(format(STATE_FOR_S_OPERATION, orderStatus));
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException(format(STATE_FOR_S_OPERATION, orderStatus));
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(final List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException(format(STATE_FOR_S_OPERATION, orderStatus));
        }
        orderStatus = OrderStatus.CANCELING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(final List<String> failureMessages) {
        if (orderStatus == OrderStatus.CANCELING || orderStatus == OrderStatus.PENDING) {
            throw new OrderDomainException(format(STATE_FOR_S_OPERATION, orderStatus));
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(final List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(getNotEmptyMessages(failureMessages));
        }
        if (failureMessages == null) {
            this.failureMessages = emptyList();
        }
    }

    private List<String> getNotEmptyMessages(List<String> failureMessages) {
        return failureMessages.stream().filter(Objects::nonNull).toList();
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem item : items) {
            item.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    private void validateItemsPrice() {
        final Money itemsPriceTotal = getPriceTotal();

        if (!price.equals(itemsPriceTotal)) {
            throw new OrderDomainException(format("Total price %s is not equal to total price of items %s!",
                    price.getAmount(), itemsPriceTotal.getAmount()));
        }
    }

    private Money getPriceTotal() {
        return items.stream()
                .map(item -> {
                    validateItemPrice(item);
                    return item.getSubTotal();
                }).reduce(Money.ZERO, Money::add);
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateItemPrice(final OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException(format("Order item price %s is not valid for product %s",
                    orderItem.getPrice().getAmount(), orderItem.getProduct().getId().getValue()));
        }
    }

    private void validateInitialOrder() {
        if (orderStatus == null || getId() == null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

}
