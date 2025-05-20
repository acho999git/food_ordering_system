package com.food.ordering.system.container;

import com.food.ordering.system.order.domain.service.service.OrderDomainService;
import com.food.ordering.system.order.domain.service.service.OrderDomainServiceImpl;
import com.food.ordering.system.payment.domain.core.service.PaymentDomainService;
import com.food.ordering.system.payment.domain.core.service.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean//this bean is needed because the PaymentDomainService is not a bean
    public PaymentDomainService orderDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
