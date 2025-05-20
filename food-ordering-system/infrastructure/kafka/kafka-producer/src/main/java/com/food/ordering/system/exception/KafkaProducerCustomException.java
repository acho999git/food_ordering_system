package com.food.ordering.system.exception;

public class KafkaProducerCustomException extends RuntimeException {

    public KafkaProducerCustomException(String message) {
        super(message);
    }
}
