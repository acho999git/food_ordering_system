package com.food.ordering.system.payment.data.access.creditentry.mapper;

import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.payment.data.access.creditentry.entity.CreditEntryEntity;
import com.food.ordering.system.payment.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.domain.core.valueobject.CreditEntryId;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryDataAccessMapper {

    public CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
        return CreditEntry.builder()
                .creditEntryId(new CreditEntryId(creditEntryEntity.getId()))
                .customerId(new CustomerId(creditEntryEntity.getCustomerId()))
                .totalAmount(new Money(creditEntryEntity.getTotalCreditAmount()))
                .build();
    }

    public CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry) {
        return CreditEntryEntity.builder()
                .id(creditEntry.getId().getValue())
                .customerId(creditEntry.getCustomerId().getValue())
                .totalCreditAmount(creditEntry.getTotalAmount().getAmount())
                .build();
    }

}
