package com.food.ordering.system.payment.domain.core.service;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.domain.common.valueobject.Money;
import com.food.ordering.system.order.domain.common.valueobject.PaymentStatus;
import com.food.ordering.system.payment.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.domain.core.entity.Payment;
import com.food.ordering.system.payment.domain.core.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.domain.core.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.domain.core.event.PaymentEvent;
import com.food.ordering.system.payment.domain.core.event.PaymentFailedEvent;
import com.food.ordering.system.payment.domain.core.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.domain.core.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.food.ordering.system.order.domain.common.constant.DomainConstants.UTC;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {


    @Override
    public PaymentEvent validateAndInitializePayment(final Payment payment,
                                                     final CreditEntry creditEntry,
                                                     final List<CreditHistory> creditHistories,
                                                     final List<String> failureMessages,
                                                     final DomainEventPublisher<PaymentCompletedEvent> paymentEventPublisher,
                                                     final DomainEventPublisher<PaymentFailedEvent> paymentFailedPublisher) {

        payment.validatePayment(failureMessages);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (failureMessages.isEmpty()) {
            log.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), paymentEventPublisher);
        } else {
            log.info("Payment initiation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages, paymentFailedPublisher);
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(final Payment payment,
                                                 final CreditEntry creditEntry,
                                                 final List<CreditHistory> creditHistories,
                                                 final List<String> failureMessages,
                                                 final DomainEventPublisher<PaymentCancelledEvent> paymentCanceledPublisher,
                                                 final DomainEventPublisher<PaymentFailedEvent> paymentFailedPublisher) {

        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

        if (failureMessages.isEmpty()) {
            log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), paymentCanceledPublisher);
        } else {
            log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages, paymentFailedPublisher);
        }
    }

    private void validateCreditEntry(final Payment payment,
                                     final CreditEntry creditEntry,
                                     final List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalAmount())) {
            log.error("Customer with id: {} doesn't have enough credit for payment!",
                    payment.getCustomerId().getValue());
            failureMessages.add("Customer with id=" + payment.getCustomerId().getValue()
                    + " doesn't have enough credit for payment!");
        }
    }

    private void subtractCreditEntry(final Payment payment, final CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(final Payment payment,
                                     final List<CreditHistory> creditHistories,
                                     final TransactionType transactionType) {
        creditHistories.add(CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                .customerId(payment.getCustomerId())
                .amount(payment.getPrice())
                .transactionType(transactionType)
                .build());
    }


    private void validateCreditHistory(final CreditEntry creditEntry,
                                       final List<CreditHistory> creditHistories,
                                       final List<String> failureMessages) {
        Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);
        Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            log.error("Customer with id: {} doesn't have enough credit according to credit history",
                    creditEntry.getCustomerId().getValue());
            failureMessages.add("Customer with id=" + creditEntry.getCustomerId().getValue() +
                    " doesn't have enough credit according to credit history!");
        }

        if (!creditEntry.getTotalAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
            log.error("Credit history total is not equal to current credit for customer id: {}!",
                    creditEntry.getCustomerId().getValue());
            failureMessages.add("Credit history total is not equal to current credit for customer id: {}" +
                    creditEntry.getCustomerId().getValue() + "!");
        }
    }

    private Money getTotalHistoryAmount(final List<CreditHistory> creditHistories,
                                        final TransactionType transactionType) {
        return creditHistories.stream()
                .filter(creditHistory -> transactionType == creditHistory.getTransactionType())
                .map(CreditHistory::getTotalAmount)
                .reduce(Money.ZERO, Money::add);
    }

    private void addCreditEntry(final Payment payment, final CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }
}
