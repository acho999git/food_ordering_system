package com.food.ordering.system.payment.domain.application.service.adapter;

import com.food.ordering.system.payment.domain.application.service.dto.PaymentRequestDto;
import com.food.ordering.system.payment.domain.application.service.port.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.domain.core.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service// this is input adapter for the message listener port
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;

    public PaymentRequestMessageListenerImpl(final PaymentRequestHelper paymentRequestHelper) {
        this.paymentRequestHelper = paymentRequestHelper;
    }

    @Override
    public void completePayment(final PaymentRequestDto paymentRequest) {
        final PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    @Override
    public void cancelPayment(final PaymentRequestDto paymentRequest) {
        final PaymentEvent paymentEvent = paymentRequestHelper.cancelPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    private void fireEvent(final PaymentEvent paymentEvent) {
        log.info("Payment event published paymentId {}, orderId {}",
                paymentEvent.getPayment().getId(),
                paymentEvent.getPayment().getOrderId());
        paymentEvent.fire();
    }
}
