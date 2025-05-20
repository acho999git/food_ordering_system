package com.food.ordering.system.order.domain.dataaccess.customer.adapter;

import com.food.ordering.system.order.domain.dataaccess.customer.entity.CustomerEntity;
import com.food.ordering.system.order.domain.dataaccess.customer.mapper.CustomerMapper;
import com.food.ordering.system.order.domain.dataaccess.customer.repository.CustomerJpaRepository;
import com.food.ordering.system.order.domain.service.entity.Customer;
import com.food.ordering.system.order.domain.common.exception.DomainException;
import com.food.ordering.system.order.domain.service.ports.output.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component//output adapter
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerRepositoryImpl(CustomerJpaRepository jpaRepository, CustomerMapper customerMapper) {
        this.jpaRepository = jpaRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public Optional<Customer> findCustomer(final UUID customer) {

        final Optional<CustomerEntity> customerEntity = jpaRepository.findById(customer);

        if (customerEntity.isEmpty()) {
            throw new DomainException("Customer not found");
        }

        return Optional.of(customerMapper.toCustomer(customerEntity.get()));
    }
}
