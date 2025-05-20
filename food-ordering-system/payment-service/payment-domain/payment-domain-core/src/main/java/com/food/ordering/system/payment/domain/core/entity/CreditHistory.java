package com.food.ordering.system.payment.domain.core.entity;

import com.food.ordering.system.order.domain.common.entity.BaseEntity;
import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.payment.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.domain.core.valueobject.TransactionType;
import lombok.Getter;


@Getter
public class CreditHistory extends BaseEntity<CreditHistoryId> {

    private final CustomerId customerId;
    private final Money totalAmount;
    private final TransactionType transactionType;

    private CreditHistory(final Builder builder) {
        setId(builder.creditHistoryId);
        this.customerId = builder.customerId;
        this.totalAmount = builder.totalAmount;
        this.transactionType = builder.transactionType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private CreditHistoryId creditHistoryId;
        private Money totalAmount;
        private CustomerId customerId;
        private TransactionType transactionType;

        public Builder creditHistoryId(final CreditHistoryId creditHistoryId) {
            this.creditHistoryId = creditHistoryId;
            return this;
        }

        public Builder transactionType(final TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder customerId(final CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder amount(final Money totalAmount){
            this.totalAmount = totalAmount;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }
}
