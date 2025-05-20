package com.food.ordering.system.order.domain.service.adapters;

import com.food.ordering.system.order.domain.service.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.domain.service.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.domain.service.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.service.mapper.OrderDtoMapper;
import com.food.ordering.system.order.domain.service.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateOrderCommandHandler {

    private final CreateOrderHelper helper;
    private final OrderDtoMapper mapper;
    private final OrderCreatedPaymentRequestMessagePublisher publisher;


    public CreateOrderCommandHandler(final CreateOrderHelper helper,
                                     final OrderDtoMapper mapper,
                                     final OrderCreatedPaymentRequestMessagePublisher publisher) {
        this.helper = helper;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    public CreateOrderResponse handleCreateOrderCommand(final CreateOrderCommand createOrderCommand) {
        final OrderCreatedEvent orderCreatedEvent = this.helper.persistOrder(createOrderCommand);
        log.info("Created order with id: {}", orderCreatedEvent.getOrder().getId());
        publisher.publish(orderCreatedEvent);
        return mapper.toCreateOrderResponse(orderCreatedEvent.getOrder(), "Order created successfully!");
    }
}
