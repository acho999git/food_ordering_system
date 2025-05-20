package com.food.ordering.system.restaurant.application.service.port.input;

import com.food.ordering.system.restaurant.application.service.dto.RestaurantApprovalRequestDto;

//input port which will be implemented in the adapter package
public interface RestaurantApprovalRequestMessageListener {

    void approveOrder(final RestaurantApprovalRequestDto approvalRequest);

}
