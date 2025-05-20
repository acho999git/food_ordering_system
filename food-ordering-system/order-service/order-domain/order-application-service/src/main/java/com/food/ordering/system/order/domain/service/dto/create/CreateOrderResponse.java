package com.food.ordering.system.order.domain.service.dto.create;

import com.food.ordering.system.order.domain.common.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {

    @NotNull
    private final UUID orderId;

    @NotNull
    private final OrderStatus status;

    @NotNull
    private final String message;

}
