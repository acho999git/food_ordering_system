package com.food.ordering.system.order.domain.service.adapters;
import com.food.ordering.system.order.domain.service.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.domain.service.ports.input.message.listener.restaturantapproval.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service//input adapter
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {
    @Override
    public void orderApproved(final RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(final RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
