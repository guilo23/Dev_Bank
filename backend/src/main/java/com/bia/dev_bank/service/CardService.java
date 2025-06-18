package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.card.CardResponse;
import com.bia.dev_bank.dto.card.CreditRequest;
import com.bia.dev_bank.dto.card.CreditUpdate;
import com.bia.dev_bank.dto.payments.CardPaymentsRequest;
import com.bia.dev_bank.dto.payments.CardPaymentsResponse;
import com.bia.dev_bank.dto.report.StatementResponse;
import com.bia.dev_bank.entity.Card;
import com.bia.dev_bank.entity.CardPayments;
import com.bia.dev_bank.entity.CreditPurchase;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardPaymentsRepository;
import com.bia.dev_bank.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

  private final CardRepository cardRepository;
  private final AccountRepository accountRepository;
  private final CardPaymentsRepository cardPaymentsRepository;

  public CardResponse cardCreate(CreditRequest request, String accountNumber) {
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
    return new CardResponse(card);
  }

  @Transactional
  public CardPaymentsResponse addCreditCardPayment(CardPaymentsRequest request) {
    var card =
        cardRepository
            .findCardByCardNumber(request.cardNumber())
            .orElseThrow(() -> new EntityNotFoundException("Card not found"));
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
    return new CardPaymentsResponse(
        card.getCardNumber(),
        payments.get(0).getProductName(),
        payments.get(0).getInstallmentNumber(),
        payments.get(0).getInstallmentAmount());
  }

  public List<StatementResponse> cardsDebitPaymentsReport(String cardNumber) {
    var card =
        cardRepository
            .findCardByCardNumber(cardNumber)
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    if (CardType.DEBIT.equals(card.getCardType())) {
      List<CardPayments> payments = cardPaymentsRepository.findByCardId(card.getId());
      return payments.stream()
          .map(
              px ->
                  new StatementResponse(
                      "Pagamento no cartão de débito",
                      px.getTotalBuying(),
                      px.getPaymentDate(),
                      String.format("Compra no débito de de um %s", px.getProductName())))
          .toList();
    } else {
      List<StatementResponse> returns = new ArrayList<>();
      returns.add(
          new StatementResponse(
              "TIPO_DESCONHECIDO", BigDecimal.ZERO, LocalDate.now(), "card type invalid"));
      return returns;
    }
  }

  public List<StatementResponse> cardsCreditPaymentsReport(String cardNumber) {
    var card =
        cardRepository
            .findCardByCardNumber(cardNumber)
            .orElseThrow(() -> new EntityNotFoundException("cardNumber not found"));
    if (CardType.CREDIT.equals(card.getCardType())) {
      List<CreditPurchase> purchases = card.getPurchases();
      return purchases.stream()
          .map(
              px ->
                  new StatementResponse(
                      "Pagamento no cartão de Crédito",
                      px.getAmount(),
                      px.getPurchaseDate(),
                      String.format("Compra no credito de de um %s", px.getProductName())))
          .toList();
    } else {
      List<StatementResponse> returns = new ArrayList<>();
      returns.add(
          new StatementResponse("", BigDecimal.ZERO, LocalDate.now(), "card type not found"));
      return returns;
    }
  }

  public CardResponse getCardById(Long id) {
    var card =
        cardRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    return new CardResponse(card);
  }

  public List<CardResponse> getAllCardByAccountNumber(String accountNumber) {
    var cards =
        cardRepository
            .findAllCardsByAccountAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("card not found"));

    return cards.stream().map(CardResponse::new).collect(Collectors.toList());
  }

  public List<Card> getAllCardForReport(String accountNumber) {
    return cardRepository
        .findAllCardsByAccountAccountNumber(accountNumber)
        .orElseThrow(() -> new EntityNotFoundException("card not found"));
  }

  @Transactional
  public CardResponse cardUpdate(CreditUpdate update, Long id) {
    var card =
        cardRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("card not found"));
    card.setCardLimit(update.cardLimit());
    return new CardResponse(card);
  }

  public void cardDelete(Long id) {
    cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("card not found"));
    cardRepository.deleteById(id);
  }

  public CardPayments addDebitCardPayment(CardPaymentsRequest request) {
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
    return payment;
  }
}
