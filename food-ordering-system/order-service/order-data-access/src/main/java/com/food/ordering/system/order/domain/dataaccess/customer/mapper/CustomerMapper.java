package com.food.ordering.system.order.domain.dataaccess.customer.mapper;

import com.food.ordering.system.order.domain.dataaccess.customer.entity.CustomerEntity;
import com.food.ordering.system.order.domain.service.entity.Customer;
import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toCustomer(final CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }

}
