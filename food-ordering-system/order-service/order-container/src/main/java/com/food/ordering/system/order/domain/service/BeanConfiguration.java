package com.food.ordering.system.order.domain.service;

import com.food.ordering.system.order.domain.service.service.OrderDomainService;
import com.food.ordering.system.order.domain.service.service.OrderDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean//this bean is needed because the OrderDomainService is not a bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }
}
