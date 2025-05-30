package com.food.ordering.system.order.domain.service.rest;

import com.food.ordering.system.order.domain.service.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.domain.service.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.domain.service.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.domain.service.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.domain.service.ports.input.service.OrderApplicationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @Autowired
    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody final CreateOrderCommand createOrderCommand) {
        log.info("Create order command: {}", createOrderCommand);
        final CreateOrderResponse order = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created id: {}", order.getOrderId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable final UUID id) {
        final TrackOrderResponse response = orderApplicationService.trackOrder(TrackOrderQuery.builder()
                .orderTrackingId(id)
                .build());
        log.info("Order tracked id: {}", response.getOrderTrackingId());
        return ResponseEntity.ok(response);
    }
}
