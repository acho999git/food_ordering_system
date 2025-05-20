package com.food.ordering.system.payment.domain.core.entity;

import com.food.ordering.system.order.domain.common.entity.BaseEntity;
import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.payment.domain.core.valueobject.CreditEntryId;
import lombok.Getter;

@Getter
public class CreditEntry extends BaseEntity<CreditEntryId> {

    private final CustomerId customerId;
    private final Money totalAmount;

    private CreditEntry(final Builder builder) {
        setId(builder.creditEntryId);
        this.customerId = builder.customerId;
        this.totalAmount = builder.totalAmount;
    }

    public void addCreditAmount(final Money creditAmount) {
        this.totalAmount.add(creditAmount);
    }

    public void subtractCreditAmount(final Money creditAmount) {
        this.totalAmount.subtract(creditAmount);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private CreditEntryId creditEntryId;
        private CustomerId customerId;
        private Money totalAmount;

        public Builder customerId(final CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder totalAmount(final Money totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder creditEntryId(final CreditEntryId creditEntryId) {
            this.creditEntryId = creditEntryId;
            return this;
        }

        public CreditEntry build(){
            return new CreditEntry(this);
        }
    }


}
