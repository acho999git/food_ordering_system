package com.food.ordering.system.order.domain.service.mapper;

import com.food.ordering.system.kafka.order.avro.model.*;
import com.food.ordering.system.order.domain.service.dto.message.PaymentResponse;
import com.food.ordering.system.order.domain.service.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.event.OrderEvent;
import com.food.ordering.system.order.domain.service.event.OrderPaidEvent;
import com.food.ordering.system.order.domain.common.valueobject.OrderApprovalStatus;
import com.food.ordering.system.order.domain.common.valueobject.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel toPaymentRequestAvroModel(final OrderEvent orderCreatedEvent,
                                                             final PaymentOrderStatus status) {
        return PaymentRequestAvroModel.newBuilder()
                .setId(orderCreatedEvent.getOrder().getOrderId().getValue())
                .setSagaId(UUID.randomUUID())
                .setCustomerId(orderCreatedEvent.getOrder().getCustomerId().getValue())
                .setOrderId(orderCreatedEvent.getOrder().getOrderId().getValue())
                .setPrice(orderCreatedEvent.getOrder().getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getTimestamp().toInstant())
                .setPaymentOrderStatus(status)
                .build();
    }

    public RestaurantApprovalRequestAvroModel toRestaurantApprovalRequestAvroModel(final OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())//this will be implemented when saga pattern is
                .setOrderId(order.getId().getValue())
                .setRestaurantId(order.getRestaurantId().getValue())
                .setOrderId(order.getId().getValue())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(order.getItems().stream().map(orderItem ->
                        Product.newBuilder()
                                .setId(orderItem.getProduct().getId().getValue().toString())
                                .setQuantity(orderItem.getQuantity())
                                .build()).collect(Collectors.toList()))
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getTimestamp().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

    public PaymentResponse toPaymentResponse(final PaymentResponseAvroModel paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .status(PaymentStatus.valueOf(
                        paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse toRestaurantApprovalResponse(final RestaurantApprovalResponseAvroModel
                                                        restaurantApprovalResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(restaurantApprovalResponseAvroModel.getId())
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
                .orderId(restaurantApprovalResponseAvroModel.getOrderId())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .status(OrderApprovalStatus.valueOf(
                        restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
                .build();
    }
}
