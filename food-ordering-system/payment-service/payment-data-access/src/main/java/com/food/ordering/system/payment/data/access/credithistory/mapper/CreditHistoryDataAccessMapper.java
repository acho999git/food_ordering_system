package com.food.ordering.system.payment.data.access.credithistory.mapper;

import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.payment.data.access.credithistory.entity.CreditHistoryEntity;
import com.food.ordering.system.payment.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.domain.core.valueobject.CreditHistoryId;
import org.springframework.stereotype.Component;

@Component
public class CreditHistoryDataAccessMapper {

    public CreditHistory creditHistoryEntityToCreditHistory(CreditHistoryEntity creditHistoryEntity) {
        return CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(creditHistoryEntity.getId()))
                .customerId(new CustomerId(creditHistoryEntity.getCustomerId()))
                .amount(new Money(creditHistoryEntity.getAmount()))
                .transactionType(creditHistoryEntity.getType())
                .build();
    }

    public CreditHistoryEntity creditHistoryToCreditHistoryEntity(CreditHistory creditHistory) {
        return CreditHistoryEntity.builder()
                .id(creditHistory.getId().getValue())
                .customerId(creditHistory.getCustomerId().getValue())
                .amount(creditHistory.getTotalAmount().getAmount())
                .type(creditHistory.getTransactionType())
                .build();
    }

}
