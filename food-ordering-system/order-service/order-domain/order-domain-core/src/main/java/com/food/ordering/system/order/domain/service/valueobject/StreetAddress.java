package com.food.ordering.system.order.domain.service.valueobject;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Builder
@Getter
public class StreetAddress {
    private final StreetAddressId streetAddressId;
    private final String street;
    private final String postalCode;
    private final String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetAddress that = (StreetAddress) o;
        return Objects.equals(street, that.street) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postalCode, city);
    }
}
