package com.food.ordering.system.order.domain.service.kafka.publisher;

import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.order.domain.service.config.OrderServiceConfigData;
import com.food.ordering.system.order.domain.service.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.service.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.order.domain.service.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.KafkaMessageHelper;
import com.food.ordering.system.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final KafkaMessageHelper orderMessageKafkaHelper;

    @Autowired
    public CreateOrderKafkaMessagePublisher(final OrderMessagingDataMapper orderMessagingDataMapper,
                                            final OrderServiceConfigData orderServiceConfigData,
                                            final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer,
                                            final KafkaMessageHelper orderMessageKafkaHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.orderMessageKafkaHelper = orderMessageKafkaHelper;
    }

    @Override
    public void publish(final OrderCreatedEvent domainEvent) {

        final OrderId orderId = domainEvent.getOrder().getOrderId();
        log.info("Received order created event - OrderId: {}", orderId);

        try{
            final PaymentRequestAvroModel paymentRequestAvroModel =
                    orderMessagingDataMapper.toPaymentRequestAvroModel(domainEvent, PaymentOrderStatus.PENDING);
            this.kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId.getValue().toString(),
                    paymentRequestAvroModel,
                    "PaymentRequestAvroModel");

            log.info("PaymentRequestAvroModel sent to Kafka for order - OrderId: {}", orderId);
        } catch (Exception e) {
            log.error("Error sending PaymentRequestAvroModel event for order OrderId: {}", orderId);
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }

    }
}
