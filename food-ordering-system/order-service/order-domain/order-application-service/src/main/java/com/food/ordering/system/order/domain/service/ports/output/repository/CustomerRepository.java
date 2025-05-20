package com.food.ordering.system.order.domain.service.ports.output.repository;

import com.food.ordering.system.order.domain.service.entity.Customer;

import java.util.Optional;
import java.util.UUID;

//output port which implementation output adapter is in order-data-access service
public interface CustomerRepository {

   Optional<Customer> findCustomer(final UUID customer);

}
