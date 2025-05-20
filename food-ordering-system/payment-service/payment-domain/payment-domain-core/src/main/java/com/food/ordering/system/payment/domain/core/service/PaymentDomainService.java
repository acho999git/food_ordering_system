package com.food.ordering.system.payment.domain.core.service;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payment.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.domain.core.entity.Payment;
import com.food.ordering.system.payment.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.domain.core.event.PaymentEvent;
import com.food.ordering.system.payment.domain.core.event.PaymentFailedEvent;

import java.util.List;

//this is the domain service for payment domain
public interface PaymentDomainService {

    PaymentEvent validateAndInitializePayment(final Payment payment,
                                              final CreditEntry creditEntry,
                                              final List<CreditHistory> creditHistories,
                                              final List<String> failureMessages,
                                              final DomainEventPublisher<PaymentCompletedEvent> paymentEventPublisher,
                                              final DomainEventPublisher<PaymentFailedEvent> paymentFailedPublisher);

    PaymentEvent validateAndCancelPayment(final Payment payment,
                                          final CreditEntry creditEntry,
                                          final List<CreditHistory> creditHistories,
                                          final List<String> failureMessages,
                                          final DomainEventPublisher<PaymentCancelledEvent> paymentCanceledPublisher,
                                          final DomainEventPublisher<PaymentFailedEvent> paymentFailedPublisher);

}
