package com.food.ordering.system.payment.messaging.mapper;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.order.domain.common.valueobject.PaymentOrderStatus;
import com.food.ordering.system.payment.domain.application.service.dto.PaymentRequestDto;
import com.food.ordering.system.payment.domain.core.entity.Payment;
import com.food.ordering.system.payment.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.domain.core.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {

    public PaymentResponseAvroModel
    paymentCompletedEventToPaymentResponseAvroModel(final PaymentCompletedEvent paymentCompletedEvent) {
        return getResponseAvroModel(paymentCompletedEvent.getPayment(), paymentCompletedEvent.getCreatedAt(), paymentCompletedEvent.getFailureMessages());
    }

    public PaymentResponseAvroModel
    paymentCancelledEventToPaymentResponseAvroModel(final PaymentCancelledEvent paymentCancelledEvent) {
        return getResponseAvroModel(paymentCancelledEvent.getPayment(), paymentCancelledEvent.getCreatedAt(), paymentCancelledEvent.getFailureMessages());
    }

    public PaymentResponseAvroModel
    paymentFailedEventToPaymentResponseAvroModel(final PaymentFailedEvent paymentFailedEvent) {
        return getResponseAvroModel(paymentFailedEvent.getPayment(), paymentFailedEvent.getCreatedAt(), paymentFailedEvent.getFailureMessages());
    }

    private static PaymentResponseAvroModel getResponseAvroModel(final Payment paymentFailedEvent,
                                                                 final ZonedDateTime paymentFailedEvent1,
                                                                 final List<String> paymentFailedEvent2) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(""))
                .setPaymentId(paymentFailedEvent.getId().getValue())
                .setCustomerId(paymentFailedEvent.getCustomerId().getValue())
                .setOrderId(paymentFailedEvent.getOrderId().getValue())
                .setPrice(paymentFailedEvent.getPrice().getAmount())
                .setCreatedAt(paymentFailedEvent1.toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(paymentFailedEvent.getPaymentStatus().name()))
                .setFailureMessages(paymentFailedEvent2)
                .build();
    }

    public PaymentRequestDto paymentRequestAvroModelToPaymentRequest(final PaymentRequestAvroModel paymentRequestAvroModel) {
        return PaymentRequestDto.builder()
                .id(paymentRequestAvroModel.getId().toString())
                .sagaId(paymentRequestAvroModel.getSagaId().toString())
                .customerId(paymentRequestAvroModel.getCustomerId().toString())
                .orderId(paymentRequestAvroModel.getOrderId().toString())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().name()))
                .build();
    }
}
