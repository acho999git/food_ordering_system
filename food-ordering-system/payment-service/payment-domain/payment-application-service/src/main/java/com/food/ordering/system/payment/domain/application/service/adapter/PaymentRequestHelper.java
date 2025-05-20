package com.food.ordering.system.payment.domain.application.service.adapter;

import com.food.ordering.system.order.domain.common.valueobject.CustomerId;
import com.food.ordering.system.payment.domain.application.service.dto.PaymentRequestDto;
import com.food.ordering.system.payment.domain.application.service.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.domain.application.service.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.domain.application.service.port.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.domain.application.service.port.output.message.publisher.PaymentCompletedMessagePublisher;
import com.food.ordering.system.payment.domain.application.service.port.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.ordering.system.payment.domain.application.service.port.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.domain.application.service.port.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.domain.application.service.port.output.repository.PaymentRepository;
import com.food.ordering.system.payment.domain.core.entity.CreditEntry;
import com.food.ordering.system.payment.domain.core.entity.CreditHistory;
import com.food.ordering.system.payment.domain.core.entity.Payment;
import com.food.ordering.system.payment.domain.core.event.PaymentEvent;
import com.food.ordering.system.payment.domain.core.service.PaymentDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentCompletedMessagePublisher paymentCompletedMessagePublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledMessagePublisher;
    private final PaymentFailedMessagePublisher paymentFailedMessagePublisher;

    public PaymentRequestHelper(final PaymentRepository paymentRepository,
                                final CreditEntryRepository creditEntryRepository,
                                final CreditHistoryRepository creditHistoryRepository,
                                final PaymentDomainService paymentDomainService,
                                final PaymentDataMapper paymentDataMapper,
                                final PaymentCompletedMessagePublisher paymentCompletedMessagePublisher,
                                final PaymentCancelledMessagePublisher paymentCancelledMessagePublisher,
                                final PaymentFailedMessagePublisher paymentFailedMessagePublisher) {
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentCompletedMessagePublisher = paymentCompletedMessagePublisher;
        this.paymentCancelledMessagePublisher = paymentCancelledMessagePublisher;
        this.paymentFailedMessagePublisher = paymentFailedMessagePublisher;
    }

    @Transactional
    public PaymentEvent persistPayment(final PaymentRequestDto paymentRequestDto) {
        log.info("Persisting payment for order {}", paymentRequestDto.getOrderId());
        final Payment payment = getPayment(paymentRequestDto);
        final PaymentEvent paymentEvent = getPaymentEvent(payment);
        paymentRepository.save(payment);
        return paymentEvent;
    }

    @Transactional
    public PaymentEvent cancelPayment(final PaymentRequestDto paymentRequestDto) {
        log.info("Received rollback payment event for order {}", paymentRequestDto.getOrderId());
        final Payment payment = getPaymentById(UUID.fromString(paymentRequestDto.getOrderId()));
        final PaymentEvent paymentEvent = getCancelPaymentEvent(payment);
        paymentRepository.save(payment);
        return paymentEvent;
    }

    private Payment getPaymentById(final UUID orderId) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);

        if (payment.isEmpty()) {
            log.error("Payment not found for orderId {}", orderId);
            throw new PaymentApplicationServiceException("Payment not found");
        }

        return payment.get();
    }

    private Payment getPayment(final PaymentRequestDto paymentRequestDto) {
        return paymentDataMapper.toPayment(paymentRequestDto);
    }

    private PaymentEvent getPaymentEvent(final Payment payment) {
        final CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        final List<CreditHistory> histories = getCreditHistories(payment.getCustomerId());
        final List<String> failureMessages = new ArrayList<>();
        final PaymentEvent paymentEvent =
                paymentDomainService.validateAndInitializePayment(payment, creditEntry, histories, failureMessages,
                        paymentCompletedMessagePublisher, paymentFailedMessagePublisher);
        saveCreditEntryAndHistory(failureMessages, creditEntry, histories);
        return paymentEvent;
    }

    private PaymentEvent getCancelPaymentEvent(final Payment payment) {
        final CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        final List<CreditHistory> histories = getCreditHistories(payment.getCustomerId());
        final List<String> failureMessages = new ArrayList<>();
        final PaymentEvent paymentEvent =
                paymentDomainService.validateAndCancelPayment(payment, creditEntry, histories, failureMessages,
                        paymentCancelledMessagePublisher, paymentFailedMessagePublisher);
        saveCreditEntryAndHistory(failureMessages, creditEntry, histories);
        return paymentEvent;
    }

    private void saveCreditEntryAndHistory(final List<String> failureMessages,
                                           final CreditEntry creditEntry,
                                           final List<CreditHistory> histories) {
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(histories.get(histories.size() - 1));
        }
    }

    private CreditEntry getCreditEntry(final CustomerId customerId) {
        final Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);

        if (creditEntry.isEmpty()) {
            log.error("No credit entry found for customer id {}", customerId);
            throw new PaymentApplicationServiceException("No credit entry found for customer id " + customerId);
        }

        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistories(final CustomerId customerId) {
        final Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);

        if (creditHistories.isEmpty()) {
            log.error("No credit history found for customer id {}", customerId);
            throw new PaymentApplicationServiceException("No credit history found for customer id " + customerId);
        }

        return creditHistories.get();
    }
}
