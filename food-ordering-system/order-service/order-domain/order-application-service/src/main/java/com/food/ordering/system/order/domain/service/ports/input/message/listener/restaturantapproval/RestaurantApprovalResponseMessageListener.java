package com.food.ordering.system.order.domain.service.ports.input.message.listener.restaturantapproval;

import com.food.ordering.system.order.domain.service.dto.message.RestaurantApprovalResponse;

//input port
public interface RestaurantApprovalResponseMessageListener {

    void orderApproved(final RestaurantApprovalResponse approvalResponse);

    void orderRejected(final RestaurantApprovalResponse approvalResponse);
//these two interfaces will be faired by domain events
}
