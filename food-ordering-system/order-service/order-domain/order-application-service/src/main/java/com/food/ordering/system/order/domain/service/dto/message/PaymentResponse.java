package com.food.ordering.system.order.domain.service.dto.message;

import com.food.ordering.system.order.domain.common.valueobject.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class PaymentResponse {

    private final UUID id;
    private final UUID sagaId;
    private final UUID orderId;
    private final UUID paymentId;
    private final UUID customerId;
    private final BigDecimal price;
    private final Instant createdAt;
    private final PaymentStatus status;
    private final List<String> failureMessages;

}
