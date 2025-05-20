package com.food.ordering.system.order.domain.service.dto.message;

import com.food.ordering.system.order.domain.common.valueobject.OrderApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalResponse {

    private final UUID id;
    private final UUID sagaId;
    private final UUID orderId;
    private final UUID restaurantId;
    private final Instant createdAt;
    private final OrderApprovalStatus status;
    private final List<String> failureMessages;

}
