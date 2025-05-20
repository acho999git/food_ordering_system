package com.food.ordering.system.payment.domain.application.service.mapper;

import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.payment.domain.application.service.dto.PaymentRequestDto;
import com.food.ordering.system.payment.domain.core.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {

    public Payment toPayment(final PaymentRequestDto paymentRequestDto) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequestDto.getId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequestDto.getId())))
                .price(new Money(paymentRequestDto.getPrice()))
                .build();
    }

}
