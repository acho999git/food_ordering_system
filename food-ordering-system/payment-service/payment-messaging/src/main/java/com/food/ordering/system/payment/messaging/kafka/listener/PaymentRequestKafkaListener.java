package com.food.ordering.system.payment.messaging.kafka.listener;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.order.domain.common.valueobject.PaymentOrderStatus;
import com.food.ordering.system.payment.domain.application.service.dto.PaymentRequestDto;
import com.food.ordering.system.payment.domain.application.service.port.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.messaging.mapper.PaymentMessagingDataMapper;
import com.ordering.system.KafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    public PaymentRequestKafkaListener(final PaymentRequestMessageListener paymentRequestMessageListener,
                                       final PaymentMessagingDataMapper paymentMessagingDataMapper) {
        this.paymentRequestMessageListener = paymentRequestMessageListener;
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${payment-service.payment-request-topic-name}")
    public void receive(final @Payload List<PaymentRequestAvroModel> messages,
                        final @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        final @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment responses received with keys:{}, partitions:{} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(message -> {
            final PaymentRequestDto requestAvroModel =
                    paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(message);
            if (requestAvroModel.getPaymentOrderStatus().equals(PaymentOrderStatus.PENDING)) {
                log.info("PaymentRequest for completing payment sent for order {}", message.getOrderId());
                paymentRequestMessageListener.completePayment(requestAvroModel);
            } else if (requestAvroModel.getPaymentOrderStatus().equals(PaymentOrderStatus.CANCELLED)) {
                log.info("Canceling payment for order {}", message.getOrderId());
                paymentRequestMessageListener.cancelPayment(requestAvroModel);
            }
        });

    }
}
