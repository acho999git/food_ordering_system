package com.food.ordering.system.order.domain.service.ports.output.repository;

import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.valueobject.TrackingId;

import java.util.Optional;
import java.util.UUID;

//output port
public interface OrderRepository {

    Order createOrder(final Order order);

    Order findById(final UUID orderId);

    Optional<Order> findOrderByTrackingId(final TrackingId trackingId);

}
