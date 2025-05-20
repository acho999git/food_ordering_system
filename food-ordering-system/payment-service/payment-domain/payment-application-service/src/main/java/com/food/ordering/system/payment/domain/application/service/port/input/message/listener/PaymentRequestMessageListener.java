package com.food.ordering.system.payment.domain.application.service.port.input.message.listener;


import com.food.ordering.system.payment.domain.application.service.dto.PaymentRequestDto;

//input port which will be implemented in adapter class in adapter package here in this service
public interface PaymentRequestMessageListener {

    void completePayment(PaymentRequestDto paymentRequest);

    void cancelPayment(PaymentRequestDto paymentRequest);
}
