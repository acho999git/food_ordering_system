package com.food.ordering.system.order.domain.service.ports.input.service;

import com.food.ordering.system.order.domain.service.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.domain.service.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.domain.service.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.domain.service.dto.track.TrackOrderResponse;
import jakarta.validation.Valid;

//input port injected in application-service controllers which is the aPI service
//the implementation adapter is here in order-application-service
public interface OrderApplicationService {
//these are events which will be used from calling service
    CreateOrderResponse createOrder(@Valid final CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid final TrackOrderQuery trackOrderQuery);
//here we put a @Vaid annotation because we have used a validation annotations in the dto classes
}
