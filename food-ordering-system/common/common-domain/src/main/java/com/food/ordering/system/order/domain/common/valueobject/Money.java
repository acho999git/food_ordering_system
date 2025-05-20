package com.food.ordering.system.order.domain.common.valueobject;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Builder
@Getter
public class Money {

    public static final Money ZERO = Money.builder().amount(BigDecimal.ZERO).build();

    private BigDecimal amount;

    public Money(final BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isGreaterThanZero() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public Money add(final Money money) {
        return new Money(this.setScale(amount.add(money.getAmount())));
    }

    public Money subtract(final Money money) {
        return new Money(this.setScale(amount.subtract(money.getAmount())));
    }

    public boolean isGreaterThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
    }

    public Money multiply(final int multiplier) {
        return new Money(this.setScale(amount.multiply(new BigDecimal(multiplier))));
    }

    private BigDecimal setScale(final BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_EVEN);//this is because of the rounding error
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
