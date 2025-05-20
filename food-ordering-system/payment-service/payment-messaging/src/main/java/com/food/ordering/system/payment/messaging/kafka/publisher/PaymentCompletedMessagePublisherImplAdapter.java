package com.food.ordering.system.payment.messaging.kafka.publisher;

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.payment.domain.application.service.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.domain.application.service.port.output.message.publisher.PaymentCompletedMessagePublisher;
import com.food.ordering.system.payment.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.messaging.mapper.PaymentMessagingDataMapper;
import com.food.ordering.system.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//this is the output adapter for the output port from payment-application-service
@Slf4j
@Component
public class PaymentCompletedMessagePublisherImplAdapter implements PaymentCompletedMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    @Autowired
    public PaymentCompletedMessagePublisherImplAdapter(final PaymentMessagingDataMapper paymentMessagingDataMapper,
                                                       final PaymentServiceConfigData paymentServiceConfigData,
                                                       final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer) {
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void publish(final PaymentCompletedEvent domainEvent) {
        final OrderId orderId = domainEvent.getPayment().getOrderId();
        log.info("Received PaymentCompleted event - OrderId: {}", orderId);
        try{

            final PaymentResponseAvroModel responseAvroModel =
                    paymentMessagingDataMapper.paymentCompletedEventToPaymentResponseAvroModel(domainEvent);

            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
                    orderId.getValue().toString(),
                    responseAvroModel,
                    "PaymentResponseAvroModel");

            log.info("PaymentRequestAvroModel sent to Kafka for order - OrderId: {}", orderId);
        } catch (Exception e) {
            log.error("Error sending PaymentRequestAvroModel event for order OrderId: {}", orderId.getValue());
        }
    }

}
