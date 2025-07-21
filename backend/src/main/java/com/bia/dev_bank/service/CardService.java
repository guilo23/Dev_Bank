package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.card.CardResponse;
import com.bia.dev_bank.dto.card.CreditRequest;
import com.bia.dev_bank.dto.card.CreditUpdate;
import com.bia.dev_bank.dto.payments.CardPaymentsRequest;
import com.bia.dev_bank.dto.payments.CardPaymentsResponse;
import com.bia.dev_bank.dto.report.StatementResponse;
import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Card;
import com.bia.dev_bank.entity.CardPayments;
import com.bia.dev_bank.entity.CreditPurchase;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardPaymentsRepository;
import com.bia.dev_bank.repository.CardRepository;
import com.bia.dev_bank.utils.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

  private static final Logger logger = LoggerFactory.getLogger(CardService.class);

  private final CardRepository cardRepository;
  private final AccountRepository accountRepository;
  private final CardPaymentsRepository cardPaymentsRepository;
  private final SecurityUtil securityUtil;

  public CardResponse cardCreate(CreditRequest request, String accountNumber) {
    logger.info("Creating card for account {}", accountNumber);
    var custumerId = securityUtil.getCurrentUserId();
    Account acc =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    if (!acc.getCustomer().getId().equals(custumerId)) {
      logger.warn(
          "User {} does not have permission to create card for account {}",
          custumerId,
          accountNumber);
      throw new AccessDeniedException("permission denied");
    }
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    BigDecimal limit =
        request.cardType() == CardType.CREDIT ? request.cardLimit() : account.getCurrentBalance();
    var card =
        new Card(
            null,
            request.cardNumber(),
            request.cardType(),
            limit,
            BigDecimal.ZERO,
            new ArrayList<>(),
            new ArrayList<>(),
            account);
    cardRepository.save(card);
    logger.info("Card {} created successfully for account {}", card.getCardNumber(), accountNumber);
    return new CardResponse(card);
  }

  @Transactional
  public CardPaymentsResponse addCreditCardPayment(CardPaymentsRequest request) {
    logger.info("Adding credit card payment for card {}", request.cardNumber());
    var cardVerify = cardRepository.findCardByCardNumber(request.cardNumber());
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(cardVerify.get().getAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("acount not found"));

    if (!account.getCustomer().getId().equals(custumerId)) {
      logger.warn(
          "User {} does not have permission to add credit card payment for card {}",
          custumerId,
          request.cardNumber());
      throw new AccessDeniedException("permission denied");
    }
    var card =
        cardRepository
            .findCardByCardNumber(request.cardNumber())
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    var currentDate = LocalDate.now();
    List<CardPayments> payments = new ArrayList<>();
    for (int i = 0; i < request.installmentNumber(); i++) {
      CardPayments payment = new CardPayments();
      payment.setInstallmentNumber(request.installmentNumber());
      payment.setCard(card);
      payment.setDueDate(currentDate.plusMonths(i + 1));
      payment.setProductName(request.productName());
      payment.setTotalBuying(request.totalBuying());
      payment.calculatedPaymentsDetails();
      payment.setPAID(PayedStatus.TO_PAY);
      payments.add(payment);
    }
    cardPaymentsRepository.saveAll(payments);
    card.getPurchases()
        .add(
            new CreditPurchase(
                null,
                request.productName(),
                request.totalBuying(),
                LocalDate.now(),
                request.installmentNumber(),
                card));
    logger.info("Credit card payment for card {} added successfully", request.cardNumber());
    return new CardPaymentsResponse(
        card.getCardNumber(),
        payments.get(0).getProductName(),
        payments.get(0).getInstallmentNumber(),
        payments.get(0).getInstallmentAmount());
  }

  public List<StatementResponse> cardsDebitPaymentsReport(String cardNumber) {
    logger.info("Generating debit payments report for card {}", cardNumber);
    var cardVerify = cardRepository.findCardByCardNumber(cardNumber);
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(cardVerify.get().getAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    if (!account.getCustomer().getId().equals(custumerId)) {
      logger.warn(
          "User {} does not have permission to generate debit payments report for card {}",
          custumerId,
          cardNumber);
      throw new AccessDeniedException("permission denied");
    }
    var card =
        cardRepository
            .findCardByCardNumber(cardNumber)
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    if (CardType.DEBIT.equals(card.getCardType())) {
      List<CardPayments> payments = cardPaymentsRepository.findByCardId(card.getId());
      logger.info("Found {} debit payments for card {}", payments.size(), cardNumber);
      return payments.stream()
          .map(
              px ->
                  new StatementResponse(
                      "debit payment",
                      px.getTotalBuying(),
                      px.getPaymentDate(),
                      String.format("debit payment %s of", px.getProductName())))
          .toList();
    } else {
      logger.warn("Card {} is not a debit card", cardNumber);
      List<StatementResponse> returns = new ArrayList<>();
      returns.add(
          new StatementResponse(
              "type unknow", BigDecimal.ZERO, LocalDate.now(), "card type invalid"));
      return returns;
    }
  }

  public List<StatementResponse> cardsCreditPaymentsReport(String cardNumber) {
    logger.info("Generating credit payments report for card {}", cardNumber);
    var cardVerify = cardRepository.findCardByCardNumber(cardNumber);
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(cardVerify.get().getAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    if (!account.getCustomer().getId().equals(custumerId)) {
      logger.warn(
          "User {} does not have permission to generate credit payments report for card {}",
          custumerId,
          cardNumber);
      throw new AccessDeniedException("permission denied");
    }
    var card =
        cardRepository
            .findCardByCardNumber(cardNumber)
            .orElseThrow(() -> new EntityNotFoundException("cardNumber not found"));
    if (CardType.CREDIT.equals(card.getCardType())) {
      List<CreditPurchase> purchases = card.getPurchases();
      logger.info("Found {} credit purchases for card {}", purchases.size(), cardNumber);
      return purchases.stream()
          .map(
              px ->
                  new StatementResponse(
                      "credit payment",
                      px.getAmount(),
                      px.getPurchaseDate(),
                      String.format("buy of one %s", px.getProductName())))
          .toList();
    } else {
      logger.warn("Card {} is not a credit card", cardNumber);
      List<StatementResponse> returns = new ArrayList<>();
      returns.add(
          new StatementResponse("", BigDecimal.ZERO, LocalDate.now(), "card type not found"));
      return returns;
    }
  }

  public CardResponse getCardById(Long id) {
    logger.info("Fetching card {}", id);
    var cardVerify = cardRepository.findById(id);
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(cardVerify.get().getAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    if (!account.getCustomer().getId().equals(custumerId)) {
      logger.warn("User {} does not have permission to fetch card {}", custumerId, id);
      throw new AccessDeniedException("permission denied");
    }
    var card =
        cardRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    logger.info("Card {} found", id);
    return new CardResponse(card);
  }

  public List<CardResponse> getAllCardByAccountNumber(String accountNumber) {
    logger.info("Fetching all cards for account {}", accountNumber);
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    if (!account.getCustomer().getId().equals(custumerId)) {
      logger.warn(
          "User {} does not have permission to fetch all cards for account {}",
          custumerId,
          accountNumber);
      throw new AccessDeniedException("permission denied");
    }

    var cards =
        cardRepository
            .findAllCardsByAccountAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    logger.info("Found {} cards for account {}", cards.size(), accountNumber);

    return cards.stream().map(CardResponse::new).collect(Collectors.toList());
  }

  public List<Card> getAllCardForReport(String accountNumber) {
    logger.info("Fetching all cards for report for account {}", accountNumber);
    return cardRepository
        .findAllCardsByAccountAccountNumber(accountNumber)
        .orElseThrow(() -> new EntityNotFoundException("card not found"));
  }

  @Transactional
  public CardResponse cardUpdate(CreditUpdate update, Long id) {
    logger.info("Updating card {}", id);
    var cardVerify = cardRepository.findById(id);
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(cardVerify.get().getAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    if (!account.getCustomer().getId().equals(custumerId)) {
      logger.warn("User {} does not have permission to update card {}", custumerId, id);
      throw new AccessDeniedException("permission denied");
    }
    var card =
        cardRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    card.setCardLimit(update.cardLimit());
    logger.info("Card {} updated successfully", id);
    return new CardResponse(card);
  }

  public void cardDelete(Long id) {
    logger.info("Deleting card {}", id);
    var cardVerify =
        cardRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(cardVerify.getAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    if (!account.getCustomer().getId().equals(custumerId)) {
      logger.warn("User {} does not have permission to delete card {}", custumerId, id);
      throw new AccessDeniedException("permission denied");
    }

    cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("card not found"));
    cardRepository.deleteById(id);
    logger.info("Card {} deleted successfully", id);
  }

  public CardPayments addDebitCardPayment(CardPaymentsRequest request) {
    logger.info("Adding debit card payment for card {}", request.cardNumber());
    var cardVerify = cardRepository.findCardByCardNumber(request.cardNumber());
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(cardVerify.get().getAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    if (!account.getCustomer().getId().equals(custumerId)) {
      logger.warn(
          "User {} does not have permission to add debit card payment for card {}",
          custumerId,
          request.cardNumber());
      throw new AccessDeniedException("permission denied");
    }
    var card =
        cardRepository
            .findCardByCardNumber(request.cardNumber())
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    CardPayments payment =
        new CardPayments(
            null,
            request.productName(),
            request.totalBuying(),
            1,
            request.totalBuying(),
            request.totalBuying(),
            null,
            LocalDate.now(),
            PayedStatus.TO_PAY,
            card,
            new ArrayList<>());
    cardPaymentsRepository.save(payment);
    logger.info("Debit card payment for card {} added successfully", request.cardNumber());
    return payment;
  }
}
