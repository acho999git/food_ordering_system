package com.food.ordering.system.order.domain.service.adapters;
import com.food.ordering.system.order.domain.common.event.EmptyEvent;
import com.food.ordering.system.order.domain.service.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.domain.service.event.OrderCancelledEvent;
import com.food.ordering.system.order.domain.service.ports.input.message.listener.restaturantapproval.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.food.ordering.system.order.domain.common.constant.DomainConstants.DELIMITER;

@Slf4j
@Validated
@Service//input adapter
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    private final OrderApprovalSaga saga;

    @Autowired
    public RestaurantApprovalResponseMessageListenerImpl(final OrderApprovalSaga saga) {
        this.saga = saga;
    }

    @Override
    public void orderApproved(final RestaurantApprovalResponse restaurantApprovalResponse) {
        saga.process(restaurantApprovalResponse);
        log.info("Order approved for orderId = {}", restaurantApprovalResponse.getOrderId());
    }

    @Override
    public void orderRejected(final RestaurantApprovalResponse restaurantApprovalResponse) {
        OrderCancelledEvent orderCancelledEvent = saga.rollback(restaurantApprovalResponse);
        log.error("Publishing order canceled event for orderId = {} with failure messages {}",
                restaurantApprovalResponse.getOrderId(),
                String.join(DELIMITER, restaurantApprovalResponse.getFailureMessages()));
        orderCancelledEvent.fire();
    }
}
