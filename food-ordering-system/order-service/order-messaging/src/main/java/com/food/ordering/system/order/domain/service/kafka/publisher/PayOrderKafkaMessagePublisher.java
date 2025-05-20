package com.food.ordering.system.order.domain.service.kafka.publisher;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.order.domain.service.config.OrderServiceConfigData;
import com.food.ordering.system.order.domain.service.event.OrderPaidEvent;
import com.food.ordering.system.order.domain.service.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.order.domain.service.ports.output.message.publisher.restaurantapproval.OrderPayedRequestRestaurantMessagePublisher;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements OrderPayedRequestRestaurantMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;

    @Autowired
    public PayOrderKafkaMessagePublisher(final OrderMessagingDataMapper orderMessagingDataMapper,
                                            final OrderServiceConfigData orderServiceConfigData,
                                            final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void publish(final OrderPaidEvent domainEvent) {
        final OrderId orderId = domainEvent.getOrder().getOrderId();
        log.info("Received OrderPaidEvent - OrderId: {}", orderId);

        try{
            final RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel =
                    orderMessagingDataMapper.toRestaurantApprovalRequestAvroModel(domainEvent);
            this.kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId.getValue().toString(),
                    restaurantApprovalRequestAvroModel,
                    "RestaurantApprovalRequestAvroModel");

            log.info("RestaurantApprovalRequestAvroModel sent to Kafka for order - OrderId: {}", orderId);
        } catch (Exception e) {
            log.error("Error sending RestaurantApprovalRequestAvroModel event for order OrderId: {}", orderId);
        }
    }
}
