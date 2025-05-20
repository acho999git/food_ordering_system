package com.food.ordering.system.order.domain.dataaccess.order.adapter;

import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.exception.OrderNotFoundException;
import com.food.ordering.system.order.domain.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.domain.dataaccess.order.mapper.OrderMapper;
import com.food.ordering.system.order.domain.dataaccess.order.repository.OrderJpaRepository;
import com.food.ordering.system.order.domain.service.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.domain.service.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

//output adapter
@Slf4j
@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderRepositoryImpl(final OrderJpaRepository orderJpaRepository, final OrderMapper orderMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Order createOrder(final Order order) {
        final OrderEntity orderEntity = orderMapper.toOrderEntity(order);
        orderJpaRepository.save(orderEntity);
        return orderMapper.toOrder(orderEntity);
    }

    @Override
    public Order findById(final UUID orderId) {
        final  Optional<OrderEntity> entity = orderJpaRepository.findById(orderId);
        if (entity.isEmpty()) {
            log.error("Order id {} not found", orderId);
            throw new OrderNotFoundException("Order not found");
        }
        return orderMapper.toOrder(entity.get());
    }

    @Override
    public Optional<Order> findOrderByTrackingId(final TrackingId trackingId) {
        final Optional<OrderEntity> entity = this.orderJpaRepository.findByTrackingId(trackingId.getValue());
        if (entity.isEmpty()) {
            throw new OrderNotFoundException("Order not found");
        }
        return Optional.of(orderMapper.toOrder(entity.get()));
    }
}
