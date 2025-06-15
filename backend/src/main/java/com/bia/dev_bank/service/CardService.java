package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.CardPaymentsDTOs.CardPaymentsRequest;
import com.bia.dev_bank.dto.CardPaymentsDTOs.CardPaymentsResponse;
import com.bia.dev_bank.dto.cardDTOs.CardRequest;
import com.bia.dev_bank.dto.cardDTOs.CardResponse;
import com.bia.dev_bank.dto.cardDTOs.CardUpdate;
import com.bia.dev_bank.entity.Card;
import com.bia.dev_bank.entity.CardPayments;
import com.bia.dev_bank.entity.LoanPayments;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardPaymentsRepository;
import com.bia.dev_bank.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CardPaymentsRepository cardPaymentsRepository;

    public CardResponse cardCreate(CardRequest request,String accountNumber) {
        var account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new EntityNotFoundException("Nenhuma conta Registrada com esse numero"));
        BigDecimal limit = request.cardType() == CardType.CREDIT
                ? request.cardLimit()
                : account.getCurrentBalance();

        var card = new Card(
                null,
                request.cardNumber(),
                request.cardType(),
                limit,
                BigDecimal.ZERO,
                new ArrayList<>(),
                account
        );
        cardRepository.save(card);
        return new CardResponse(card);
    }
    @Transactional
    public CardPaymentsResponse addCreditCardPayment(CardPaymentsRequest request){
        var card = cardRepository.findCardByCardNumber(request.cardNumber()).orElseThrow(
                () -> new EntityNotFoundException("nenhum cartão registrado com esse numero"));
        var currentDate = LocalDate.now();
        List<CardPayments> payments = new ArrayList<>();
        for (int i = 0; i < request.installmentNumber(); i++) {
            CardPayments payment = new CardPayments();
            payment.setInstallmentNumber(request.installmentNumber());
            payment.setCard(card);
            payment.setDueDate(currentDate.plusMonths(i+1));
            payment.setProductName(request.productName());
            payment.setTotalBuying(request.totalBuying());
            payment.calculatedPaymentsDetails();
            payment.setPAID(PayedStatus.TO_PAY);
            payments.add(payment);
        }
        cardPaymentsRepository.saveAll(payments);
        return new CardPaymentsResponse(
                card.getCardNumber(),
                payments.get(0).getProductName(),
                payments.get(0).getInstallmentNumber(),
                payments.get(0).getInstallmentAmount());
    }

    public CardResponse getCardById(Long id) {
        var card = cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Nenhum cartão Registrado com esse Id:"));
        return new CardResponse(card);

    }

    public List<CardResponse> getAllCardByAccountNumber(String accountNumber) {
        var cards = cardRepository.findAllCardsByAccountAccountNumber(accountNumber).orElseThrow(
                () -> new EntityNotFoundException("Não há cartões Registrados para essa conta:"));

        return cards.stream().map(CardResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public CardResponse cardUpdate(CardUpdate update, Long id) {
        var card = cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Nenhum cartão Registrado com esse Id:"));
        card.setCardLimit(update.cardLimit());
        return new CardResponse(card);
    }

    public void cardDelete(Long id) {
        cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Nenhum cartão Registrado com esse Id:"));
        cardRepository.deleteById(id);
    }
    public CardPayments addDebitCardPayment(CardPaymentsRequest request) {
        var card = cardRepository.findCardByCardNumber(request.cardNumber()).orElseThrow(
                () -> new EntityNotFoundException("nenhum cartão registrado com esse numero"));
        List<CardPayments> payments = new ArrayList<>();
        CardPayments payment = new CardPayments(
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
                new ArrayList<>()
        );
        cardPaymentsRepository.save(payment);
        return payment;
    }
}
