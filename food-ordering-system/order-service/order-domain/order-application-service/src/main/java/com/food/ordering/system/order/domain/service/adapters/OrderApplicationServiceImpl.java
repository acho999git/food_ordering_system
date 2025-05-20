package com.food.ordering.system.order.domain.service.adapters;

import com.food.ordering.system.order.domain.service.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.domain.service.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.domain.service.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.domain.service.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.domain.service.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j//input adapter
@Validated // here we added this annotation because of the @Valid
// annotation in the command and query objects and their @NotNull annotations on their fields
@Service
public class OrderApplicationServiceImpl implements OrderApplicationService {

    private final CreateOrderCommandHandler createOrderCommandHandler;
    private final TrackOrderCommandHandler trackOrderCommandHandler;

    public OrderApplicationServiceImpl(final CreateOrderCommandHandler createOrderCommandHandler,
                                       final TrackOrderCommandHandler trackOrderCommandHandler) {
        this.createOrderCommandHandler = createOrderCommandHandler;
        this.trackOrderCommandHandler = trackOrderCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(final CreateOrderCommand createOrderCommand) {
        return this.createOrderCommandHandler.handleCreateOrderCommand(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(final TrackOrderQuery trackOrderQuery) {
        return this.trackOrderCommandHandler.handleTrackOrderQueryCommand(trackOrderQuery);
    }
}
