package com.food.ordering.system.payment.domain.application.service.port.output.repository;

import com.food.ordering.system.payment.domain.core.entity.Payment;

import java.util.Optional;
import java.util.UUID;

//output port which will be implemented in payment-data-access service adapters
public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(UUID orderId);
}
