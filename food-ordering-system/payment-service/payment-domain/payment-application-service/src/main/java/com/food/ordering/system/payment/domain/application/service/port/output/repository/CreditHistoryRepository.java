package com.food.ordering.system.payment.domain.application.service.port.output.repository;

import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import com.food.ordering.system.payment.domain.core.entity.CreditHistory;

import java.util.List;
import java.util.Optional;

//output port which will be implemented in payment-data-access service adapters
public interface CreditHistoryRepository {

    CreditHistory save(final CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(final CustomerId customerId);
}
