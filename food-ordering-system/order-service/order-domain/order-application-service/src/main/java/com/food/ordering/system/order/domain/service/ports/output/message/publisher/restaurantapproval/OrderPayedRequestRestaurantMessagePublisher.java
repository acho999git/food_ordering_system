package com.food.ordering.system.order.domain.service.ports.output.message.publisher.restaurantapproval;

import com.food.ordering.system.order.domain.service.event.OrderPaidEvent;
import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
//Output port
public interface OrderPayedRequestRestaurantMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {

    void publish(final OrderPaidEvent domainEvent);
}
