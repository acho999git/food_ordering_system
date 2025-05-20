package com.food.ordering.system.payment.messaging.kafka.publisher;

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.payment.domain.application.service.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.domain.application.service.port.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.domain.application.service.port.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.ordering.system.payment.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.domain.core.event.PaymentFailedEvent;
import com.food.ordering.system.payment.messaging.mapper.PaymentMessagingDataMapper;
import com.food.ordering.system.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//this is the output adapter for the output port from payment-application-service
@Slf4j
@Component
public class PaymentFailedMessagePublisherImplAdapter implements PaymentFailedMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    @Autowired
    public PaymentFailedMessagePublisherImplAdapter(final PaymentMessagingDataMapper paymentMessagingDataMapper,
                                                    final PaymentServiceConfigData paymentServiceConfigData,
                                                    final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer) {
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void publish(final PaymentFailedEvent domainEvent) {
        final OrderId orderId = domainEvent.getPayment().getOrderId();
        log.info("Received PaymentFailedEvent event - OrderId: {}", orderId);
        try{

            final PaymentResponseAvroModel responseAvroModel =
                    paymentMessagingDataMapper.paymentFailedEventToPaymentResponseAvroModel(domainEvent);

            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
                    orderId.getValue().toString(),
                    responseAvroModel,
                    "PaymentResponseAvroModel");

            log.info("PaymentResponseAvroModel sent to Kafka for order - OrderId: {}", orderId);
        } catch (Exception e) {
            log.error("Error sending PaymentResponseAvroModel event for order OrderId: {}", orderId.getValue());
        }
    }

}
