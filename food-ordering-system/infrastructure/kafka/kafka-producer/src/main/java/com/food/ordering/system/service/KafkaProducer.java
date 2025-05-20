package com.food.ordering.system.service;

import org.apache.avro.specific.SpecificRecordBase;

import java.io.Serializable;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
    void send(final String topicName, final K key, final V message, final String avroModelName);
}
