package com.food.ordering.system;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaMessageHelper {

    public <T> CompletableFuture<SendResult<String, T>>
    getKafkaCallback(final String responseTopicName,
                     final T avroModel,
                     final String orderId,
                     final String avroModelName) {
        return new CompletableFuture<SendResult<String, T>>().thenApply( result -> {
            RecordMetadata metadata = result.getRecordMetadata();
            log.info("Received successful response from Kafka for order id: {}" +
                            " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                    orderId,
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset(),
                    metadata.timestamp());

            return result;
        });
//        {
//
//            @Override
//            public boolean completeThrowable(Throwable ex) {
//
//            }
//
//        };
    }
}
