package com.food.ordering.system.order.domain.service.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderCommand {
    @NotNull//these annotations will be working if we put a @Validated annotation on the service where this class is used
    private final UUID customerId;
    @NotNull
    private final UUID restaurantId;
    @NotNull
    private final OrderAddressDto address;
    @NotNull
    private final BigDecimal price;
    @NotNull
    private final List<OrderItemDto> items;
}
