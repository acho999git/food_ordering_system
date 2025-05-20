package com.food.ordering.system.order.domain.service.ports.input.message.listener.payment;

import com.food.ordering.system.order.domain.service.dto.message.PaymentResponse;

//input port
public interface PaymentResponseMessageListener {

    void paymentCompleted(final PaymentResponse paymentResponse);

    void paymentCanceled(final PaymentResponse paymentResponse);

}
