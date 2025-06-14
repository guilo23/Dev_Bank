package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.cardDTOs.CardRequest;
import com.bia.dev_bank.dto.cardDTOs.CardResponse;
import com.bia.dev_bank.dto.cardDTOs.CardUpdate;
import com.bia.dev_bank.entity.Card;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardResponse cardCreate(CardRequest request,String accountNumber) {
        var account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new EntityNotFoundException("Nenhuma conta Registrada com esse numero"));
        BigDecimal limit = request.cardType() == CardType.CREDIT
                ? request.cardLimit()
                : BigDecimal.valueOf(account.getCurrentBalance());

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
}
