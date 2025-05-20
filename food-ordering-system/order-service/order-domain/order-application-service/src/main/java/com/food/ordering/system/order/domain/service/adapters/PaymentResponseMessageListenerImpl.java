package com.food.ordering.system.order.domain.service.adapters;

import com.food.ordering.system.order.domain.service.dto.message.PaymentResponse;
import com.food.ordering.system.order.domain.service.event.OrderPaidEvent;
import com.food.ordering.system.order.domain.service.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.food.ordering.system.order.domain.common.constant.DomainConstants.DELIMITER;

@Slf4j
@Validated
@Service//input adapter
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga saga;

    public PaymentResponseMessageListenerImpl(final OrderPaymentSaga saga) {
        this.saga = saga;
    }

    @Override
    public void paymentCompleted(final PaymentResponse paymentResponse) {
        final OrderPaidEvent orderPaidEvent = saga.process(paymentResponse);
        log.info("Order Payment completed for orderId = {}", paymentResponse.getOrderId());
        orderPaidEvent.fire();
    }

    @Override
    public void paymentCanceled(final PaymentResponse paymentResponse) {
        saga.rollback(paymentResponse);
        log.error("Order is roll backed for orderId = {} with failure messages {}",
                paymentResponse.getOrderId(),
                String.join(DELIMITER, paymentResponse.getFailureMessages()));
    }
}
