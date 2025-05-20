package com.food.ordering.system.restaurant.domain.core.service;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.restaurant.domain.core.entity.Restaurant;
import com.food.ordering.system.restaurant.domain.core.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.domain.core.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.domain.core.event.OrderRejectedEvent;

import java.util.List;

public interface RestaurantDomainService {

    OrderApprovalEvent validateOrder(Restaurant restaurant,
                                     List<String> failureMessages,
                                     DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher,
                                     DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher);
}
