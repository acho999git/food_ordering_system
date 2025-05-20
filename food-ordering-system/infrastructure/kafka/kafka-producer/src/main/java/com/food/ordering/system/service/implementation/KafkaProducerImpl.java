package com.food.ordering.system.service.implementation;

import com.food.ordering.system.exception.KafkaProducerCustomException;
import com.food.ordering.system.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Autowired
    public KafkaProducerImpl(final KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(final String topicName, final K key, final V message, final String avroModelName) {
        log.info("Sending message={} to topic={}", message, topicName);
        try {
            final CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
            kafkaResultFuture.whenComplete(getSendResultThrowableBiConsumer(topicName, key, message, avroModelName));
        } catch (final KafkaException e) {
            log.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message,
                    e.getMessage());
            throw new KafkaProducerCustomException("Error on kafka producer with key: " + key + " and message: "
                    + message);
        }
    }

    private BiConsumer<SendResult<K, V>, Throwable> getSendResultThrowableBiConsumer(final String topicName,
                                                                                     final K key,
                                                                                     final V message,
                                                                                     final String avroModelName) {
        return (result, ex) -> {
            if (ex != null) {
                onFailure(topicName, message, avroModelName, ex);
            } else {
                onSuccess(key, result);
            }
        };
    }

    private void onFailure(final String topicName, final V message, final String avroModelName, final Throwable ex) {
        log.error("Error while sending " + avroModelName +
                " message {} to topic {}", message.toString(), topicName, ex);
    }

    private void onSuccess(final K key, final SendResult<K, V> result) {
        RecordMetadata metadata = result.getRecordMetadata();
        log.info("Received successful response from Kafka for order id: {}" +
                        " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                key,
                metadata.topic(),
                metadata.partition(),
                metadata.offset(),
                metadata.timestamp());
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }

}
