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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardPaymentsService {
  private final CardPaymentsRepository cardPaymentsRepository;
  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;
  private final AccountService accountService;
  private final SecurityUtil securityUtil;

  public CardPaymentsResponse getCardPaymentsById(Long id) {
    var payment =
        cardPaymentsRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("report not found"));
    return new CardPaymentsResponse(payment);
  }

  public void updatePaidAmount(Long cardPaymentId) {
    var payment =
        cardPaymentsRepository
            .findById(cardPaymentId)
            .orElseThrow(() -> new EntityNotFoundException("report not found"));
    BigDecimal totalPaid =
        payment.getTransactions().stream()
            .map(t -> t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    System.out.println(totalPaid);
    payment.setPaidAmount(totalPaid);
    updateCardPaymentStatus(payment);
    cardPaymentsRepository.save(payment);
  }

  public List<CardPaymentsResponse> getCardPaymentsreportByid(Long id) {
    var payments = cardPaymentsRepository.findAllByCardId(id);
    return payments.stream().map(CardPaymentsResponse::new).toList();
  }

  @Transactional
  public TransactionResponse addTransactionToCardPayments(Long cardPaymentId) {
    var cardVerify = cardPaymentsRepository.findById(cardPaymentId);
    var accountNumber = cardVerify.get().getCard().getAccount().getAccountNumber();
    var custumerId = securityUtil.getCurrentUserId();
    Account acc =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    if (!acc.getCustomer().getId().equals(custumerId)) {
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
    System.out.println(payment.getTotalBuying());
    updatePaidAmount(payment.getId());
    return new TransactionResponse(
        saved.getAmount(),
        "",
        saved.getOriginAccount().getCustomer().getName(),
        saved.getTransactionDate());
  }

  public void updateCardPaymentStatus(CardPayments payment) {
    BigDecimal expected =
        payment.getInstallmentAmount() != null ? payment.getInstallmentAmount() : BigDecimal.ZERO;
    BigDecimal paid = payment.getPaidAmount() != null ? payment.getPaidAmount() : BigDecimal.ZERO;

    LocalDate today = LocalDate.now();

    if (paid.compareTo(expected) >= 0) {
      payment.setPAID(PayedStatus.PAYED);
      payment.setPaymentDate(today);
    } else if (paid.compareTo(expected) < 0) {
      payment.setPAID(PayedStatus.PARTIAL);
      payment.setPaymentDate(today);
    }
  }
}
