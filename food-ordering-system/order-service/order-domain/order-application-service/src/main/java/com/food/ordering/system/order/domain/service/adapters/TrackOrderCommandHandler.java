package com.food.ordering.system.order.domain.service.adapters;

import com.food.ordering.system.order.domain.service.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.domain.service.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.exception.OrderNotFoundException;
import com.food.ordering.system.order.domain.service.mapper.OrderDtoMapper;
import com.food.ordering.system.order.domain.service.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.domain.service.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TrackOrderCommandHandler {

    private final OrderRepository orderRepository;
    private final OrderDtoMapper mapper;

    @Autowired
    public TrackOrderCommandHandler(final OrderRepository orderRepository, final OrderDtoMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public TrackOrderResponse handleTrackOrderQueryCommand(final TrackOrderQuery trackOrderQuery) {
        final Optional<Order> response =
                this.orderRepository.findOrderByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));

        if (response.isEmpty()) {
            log.info("Track order with id: {}", trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Track order with id: " + trackOrderQuery.getOrderTrackingId());
        }

        return mapper.toTrackOrderResponse(response.get());
    }

}
