package com.food.ordering.system.order.domain.dataaccess.order.mapper;

import com.food.ordering.system.order.domain.common.valueobject.*;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.entity.OrderItem;
import com.food.ordering.system.order.domain.service.entity.Product;
import com.food.ordering.system.order.domain.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.domain.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.domain.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.domain.service.valueobject.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static com.food.ordering.system.order.domain.common.constant.DomainConstants.DELIMITER;

@Component
public class OrderMapper {

    public OrderEntity toOrderEntity(final Order order) {
        final OrderEntity orderEntity = OrderEntity.builder()
                .id(UUID.randomUUID())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(UUID.randomUUID())
                .orderStatus(OrderStatus.PENDING)
                .failureMessages("NoMessages")
                .price(order.getPrice().getAmount())
                .orderAddress(getOrderAddress(order))
                .items(getOrderItemEntities(order.getItems()))
                .build();
        orderEntity.getOrderAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
        return orderEntity;
    }

    public Order toOrder(final OrderEntity order) {
        return Order.builder()
                .orderId(new OrderId(order.getId()))
                .customerId(new CustomerId(order.getCustomerId()))
                .restaurantId(new RestaurantId(order.getRestaurantId()))
                .items(getOrderItems(order.getItems()))
                .price(new Money(order.getPrice()))
                .deliveryAddress(getDeliveryAddress(order))
                .failureMessages(getFailureMessages(order))
                .orderStatus(order.getOrderStatus())
                .trackingId(new TrackingId(order.getTrackingId()))
                .build();
    }

    public OrderAddressEntity toOrderItemEntity(final StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.getStreetAddressId().getValue())
                .street(deliveryAddress.getStreet())
                .postalCode(deliveryAddress.getPostalCode())
                .city(deliveryAddress.getCity() )
                .build();

    }

    private List<String> getFailureMessages(final OrderEntity order) {
        return order.getFailureMessages() != null ?
                Arrays.stream(order.getFailureMessages().split(DELIMITER)).toList() : Collections.emptyList();
    }

    private StreetAddress getDeliveryAddress(final OrderEntity order) {
        return StreetAddress.builder()
                .street(order.getOrderAddress().getStreet())
                .postalCode(order.getOrderAddress().getPostalCode())
                .city(order.getOrderAddress().getCity())
                .build();
    }

    private List<OrderItemEntity> getOrderItemEntities(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::getOrderItem).toList();
    }

    private OrderItemEntity getOrderItem(final OrderItem order) {
        return OrderItemEntity.builder()
                .id(new Random().nextLong(1, Long.MAX_VALUE))
                .quantity(order.getQuantity())
                .price(order.getPrice().getAmount())
                .productId(order.getProduct().getId().getValue())
                .subTotal(order.getSubTotal().getAmount())
                .build();
    }

    private List<OrderItem> getOrderItems(final List<OrderItemEntity> orderItems) {
        return orderItems.stream()
                .map(this::getOrder).toList();
    }

    private OrderItem getOrder(final OrderItemEntity order) {
        final OrderItem orderItem = OrderItem.builder()
                .product(new Product(new ProductId(order.getProductId())))
                .price(new Money(order.getPrice()))
                .subTotal(new Money(order.getSubTotal()))
                .quantity(order.getQuantity())
                .build();
        orderItem.setId(new OrderItemId(order.getId()));
        return orderItem;
    }

    private OrderAddressEntity getOrderAddress(final Order order) {
        return OrderAddressEntity.builder()
                .id(UUID.randomUUID())
                .street(order.getDeliveryAddress().getStreet())
                .postalCode(order.getDeliveryAddress().getPostalCode())
                .city(order.getDeliveryAddress().getCity())
                .build();
    }

}
