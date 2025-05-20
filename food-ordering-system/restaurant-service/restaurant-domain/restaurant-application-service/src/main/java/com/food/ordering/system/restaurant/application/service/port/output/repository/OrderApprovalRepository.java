package com.food.ordering.system.restaurant.application.service.port.output.repository;

import com.food.ordering.system.restaurant.domain.core.entity.OrderApproval;

public interface OrderApprovalRepository {
    OrderApproval save(final OrderApproval orderApproval);
}
