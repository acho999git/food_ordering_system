package com.food.ordering.system.payment.domain.core.entity;

import com.food.ordering.system.order.domain.common.entity.AggregateRoot;
import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.order.domain.common.valueobject.PaymentStatus;
import com.food.ordering.system.payment.domain.core.valueobject.PaymentId;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.food.ordering.system.order.domain.common.constant.DomainConstants.UTC;
import static java.util.Objects.isNull;

@Getter
public class Payment extends AggregateRoot<PaymentId> {

    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money price;
    private PaymentStatus paymentStatus;
    private ZonedDateTime createdAt;

    private Payment(final Builder builder) {
        setId(builder.paymentId);
        this.orderId = builder.orderId;
        this.customerId = builder.customerId;
        this.price = builder.price;
        this.paymentStatus = builder.paymentStatus;
        this.createdAt = builder.createdAt;
    }

    public void initializePayment() {
        setId(new PaymentId(UUID.randomUUID()));
        createdAt = ZonedDateTime.now(ZoneId.of(UTC));
    }

    public void updateStatus(final PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void validatePayment(final List<String> failureMessages) {
        if (!price.isGreaterThanZero() || isNull(price)) {
            failureMessages.add("Invalid payment price");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{

        private PaymentId paymentId;
        private OrderId orderId;
        private CustomerId customerId;
        private Money price;
        private PaymentStatus paymentStatus;
        private ZonedDateTime createdAt;

        public Builder paymentId(final PaymentId paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder orderId(final OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder customerId(final CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder price(final Money price){
            this.price = price;
            return this;
        }

        public Builder paymentStatus(final PaymentStatus paymentStatus){
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Builder createdAt(final ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}
