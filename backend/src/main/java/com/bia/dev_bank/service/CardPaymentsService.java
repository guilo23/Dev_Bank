package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.CardPaymentsDTOs.CardPaymentsResponse;
import com.bia.dev_bank.dto.transactionDTOs.TransactionRequest;
import com.bia.dev_bank.dto.transactionDTOs.TransactionResponse;
import com.bia.dev_bank.entity.CardPayments;
import com.bia.dev_bank.entity.LoanPayments;
import com.bia.dev_bank.entity.Transaction;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardPaymentsRepository;
import com.bia.dev_bank.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardPaymentsService {
    private final CardPaymentsRepository cardPaymentsRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public CardPaymentsResponse getCardPaymentsById(Long id){
        var payment = cardPaymentsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Recebibo de pagamento não gerado"));
        return  new CardPaymentsResponse(payment);
    }
    public void updatePaidAmount(Long cardPaymentId) {
        var payment = cardPaymentsRepository.findById(cardPaymentId).orElseThrow(
                () -> new EntityNotFoundException("Recebibo de pagamento não gerado"));
        BigDecimal totalPaid = payment.getTransactions().stream()
                .map(t -> t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(totalPaid);
        payment.setPaidAmount(totalPaid);
        updateCardPaymentStatus(payment);
        cardPaymentsRepository.save(payment);
    }
    @Transactional
    public TransactionResponse addTransactionToCardPayments(Long cardPaymentId) {
        var payment = cardPaymentsRepository.findById(cardPaymentId).orElseThrow(
                () -> new EntityNotFoundException("Recebibo de pagamento não gerado"));
        var account = accountRepository.findByAccountNumber(payment.getCard().getAccount().getAccountNumber()).orElseThrow(()-> new EntityNotFoundException("NOT FOUND"));
        List<Transaction> transactionList = new ArrayList<>();
        var transaction = new Transaction(
                null,
                payment.getTotalBuying(),
                null,
                account,
                null,
                payment,
                LocalDate.now()
        );
        accountService.debit(account.getAccountNumber(),payment.getTotalBuying());
        Transaction saved = transactionRepository.save(transaction);
        payment.getTransactions().add(saved);
        System.out.println(payment.getTotalBuying());
        updatePaidAmount(payment.getId());
        return new TransactionResponse(saved.getAmount(),"",saved.getOriginAccount().getCustomer().getName(),saved.getTransactionDate());
    }

    public void updateCardPaymentStatus(CardPayments payment) {
        BigDecimal expected = payment.getInstallmentAmount() != null ? payment.getInstallmentAmount() : BigDecimal.ZERO;
        BigDecimal paid = payment.getPaidAmount() != null ? payment.getPaidAmount() : BigDecimal.ZERO;

        LocalDate today = LocalDate.now();
        LocalDate scheduledDate = payment.getDueDate();

        System.out.println(paid);

        if (paid.compareTo(expected) >= 0) {
            payment.setPAID(PayedStatus.PAYED);
            payment.setPaymentDate(today);
        } else if (paid.compareTo(expected) < 0) {
            payment.setPAID(PayedStatus.PARTIAL);
            payment.setPaymentDate(today);
        }
    }
}
