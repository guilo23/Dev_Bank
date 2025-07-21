package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.payments.CardPaymentsResponse;
import com.bia.dev_bank.dto.transaction.TransactionResponse;
import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.CardPayments;
import com.bia.dev_bank.entity.Transaction;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardPaymentsRepository;
import com.bia.dev_bank.repository.TransactionRepository;
import com.bia.dev_bank.utils.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardPaymentsService {
  private static final Logger logger = LoggerFactory.getLogger(CardPaymentsService.class);
  private final CardPaymentsRepository cardPaymentsRepository;
  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;
  private final AccountService accountService;
  private final SecurityUtil securityUtil;

  public CardPaymentsResponse getCardPaymentsById(Long id) {
    logger.info("Fetching card payment {}", id);
    var payment =
        cardPaymentsRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("report not found"));
    logger.info("Card payment {} found", id);
    return new CardPaymentsResponse(payment);
  }

  public void updatePaidAmount(Long cardPaymentId) {
    logger.info("Updating paid amount for card payment {}", cardPaymentId);
    var payment =
        cardPaymentsRepository
            .findById(cardPaymentId)
            .orElseThrow(() -> new EntityNotFoundException("report not found"));
    BigDecimal totalPaid =
        payment.getTransactions().stream()
            .map(t -> t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    logger.debug("Total paid for card payment {}: {}", cardPaymentId, totalPaid);
    payment.setPaidAmount(totalPaid);
    updateCardPaymentStatus(payment);
    cardPaymentsRepository.save(payment);
    logger.info("Paid amount for card payment {} updated successfully", cardPaymentId);
  }

  @Transactional
  public TransactionResponse addTransactionToCardPayments(Long cardPaymentId) {
    logger.info("Adding transaction to card payment {}", cardPaymentId);
    var cardVerify = cardPaymentsRepository.findById(cardPaymentId);
    var accountNumber = cardVerify.get().getCard().getAccount().getAccountNumber();
    var custumerId = securityUtil.getCurrentUserId();
    Account acc =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    if (!acc.getCustomer().getId().equals(custumerId)) {
      logger.warn(
          "User {} does not have permission to add transaction to card payment {}",
          custumerId,
          cardPaymentId);
      throw new AccessDeniedException("permission denied");
    }

    var payment =
        cardPaymentsRepository
            .findById(cardPaymentId)
            .orElseThrow(() -> new EntityNotFoundException("report not found"));
    var account =
        accountRepository
            .findByAccountNumber(payment.getCard().getAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("not found"));
    var transaction =
        new Transaction(
            null, payment.getTotalBuying(), null, account, null, payment, LocalDate.now());
    accountService.debit(account.getAccountNumber(), payment.getTotalBuying());
    Transaction saved = transactionRepository.save(transaction);
    payment.getTransactions().add(saved);
    logger.debug("Total buying for card payment {}: {}", cardPaymentId, payment.getTotalBuying());
    updatePaidAmount(payment.getId());
    logger.info("Transaction added to card payment {} successfully", cardPaymentId);
    return new TransactionResponse(
        saved.getAmount(),
        "",
        saved.getOriginAccount().getCustomer().getName(),
        saved.getTransactionDate());
  }

  public void updateCardPaymentStatus(CardPayments payment) {
    logger.info("Updating status for card payment {}", payment.getId());
    BigDecimal expected =
        payment.getInstallmentAmount() != null ? payment.getInstallmentAmount() : BigDecimal.ZERO;
    BigDecimal paid = payment.getPaidAmount() != null ? payment.getPaidAmount() : BigDecimal.ZERO;

    LocalDate today = LocalDate.now();

    if (paid.compareTo(expected) >= 0) {
      payment.setPAID(PayedStatus.PAYED);
      payment.setPaymentDate(today);
      logger.info("Card payment {} status updated to PAYED", payment.getId());
    } else if (paid.compareTo(expected) < 0) {
      payment.setPAID(PayedStatus.PARTIAL);
      payment.setPaymentDate(today);
      logger.info("Card payment {} status updated to PARTIAL", payment.getId());
    }
  }
}
