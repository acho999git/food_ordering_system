package com.food.ordering.system.payment.domain.application.service.port.output.repository;


import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import com.food.ordering.system.payment.domain.core.entity.CreditEntry;

import java.util.Optional;

//output port which will be implemented in payment-data-access service adapters
public interface CreditEntryRepository {

    CreditEntry save(final CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(final CustomerId customerId);
}
