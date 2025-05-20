package com.food.ordering.system.order.domain.service.mapper;

import com.food.ordering.system.order.domain.common.valueobject.*;
import com.food.ordering.system.order.domain.service.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.domain.service.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.domain.service.dto.create.OrderAddressDto;
import com.food.ordering.system.order.domain.service.dto.create.OrderItemDto;
import com.food.ordering.system.order.domain.service.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.entity.OrderItem;
import com.food.ordering.system.order.domain.service.entity.Product;
import com.food.ordering.system.order.domain.service.entity.Restaurant;
import com.food.ordering.system.order.domain.service.valueobject.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDtoMapper {

    public Restaurant toRestaurant(final CreateOrderCommand createOrderCommand) {
       return new Restaurant(getProducts(createOrderCommand.getItems()),
               true,
               createOrderCommand.getRestaurantId());
    }

    public Order toOrder(final CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(toStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(toOrderItems(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse toCreateOrderResponse(final Order order, final String message) {
        return CreateOrderResponse.builder()
                .orderId(order.getId().getValue())
                .status(order.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse toTrackOrderResponse(final Order order) {
        return TrackOrderResponse.builder()
                .orderStatus(order.getOrderStatus())
                .orderTrackingId(order.getId().getValue())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    private List<OrderItem> toOrderItems(final List<OrderItemDto> items) {
        return items.stream().map(this::toOrderItem).toList();
    }

    private OrderItem toOrderItem(final OrderItemDto orderItemDto) {
        return OrderItem.builder()
                .price(new Money(orderItemDto.getPrice()))
                .quantity(orderItemDto.getQuantity())
                .product(new Product(new ProductId(orderItemDto.getProductId())))
                .quantity(orderItemDto.getQuantity())
                .subTotal(new Money(orderItemDto.getSubTotal()))
                .build();
    }

    private List<Product> getProducts(final List<OrderItemDto> items) {
        return items.stream()
                .map(item -> new Product(new ProductId(item.getProductId()))).toList();
    }

    private StreetAddress toStreetAddress(final OrderAddressDto orderAddressDto) {
        return StreetAddress.builder()
                .streetAddressId(new StreetAddressId(UUID.randomUUID()))
                .street(orderAddressDto.getStreet())
                .city(orderAddressDto.getCity())
                .postalCode( orderAddressDto.getPostalCode())
                .build();
    }

}
