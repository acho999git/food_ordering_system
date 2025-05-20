package com.food.ordering.system.restaurant.application.service.adapter;

import com.food.ordering.system.restaurant.application.service.dto.RestaurantApprovalRequestDto;
import com.food.ordering.system.restaurant.application.service.port.input.RestaurantApprovalRequestMessageListener;
import com.food.ordering.system.restaurant.domain.core.event.OrderApprovalEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//input adapter implementation of input port
@Slf4j
@Service
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {

    private final RestaurantApprovalRequestHelper restaurantRequestHelper;

    @Autowired
    public RestaurantApprovalRequestMessageListenerImpl(final RestaurantApprovalRequestHelper restaurantRequestHelper) {
        this.restaurantRequestHelper = restaurantRequestHelper;
    }

    @Override
    public void approveOrder(final RestaurantApprovalRequestDto approvalRequest) {
        OrderApprovalEvent orderApprovalEvent =
                restaurantRequestHelper.persistOrderApproval(approvalRequest);
        orderApprovalEvent.fire();
    }
}
