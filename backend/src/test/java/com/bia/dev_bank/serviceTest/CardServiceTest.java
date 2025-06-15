package com.bia.dev_bank.serviceTest;
import com.bia.dev_bank.dto.cardDTOs.CardRequest;
import com.bia.dev_bank.dto.cardDTOs.CardResponse;
import com.bia.dev_bank.dto.cardDTOs.CardUpdate;
import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Card;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.enums.AccountType;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardRepository;
import com.bia.dev_bank.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    private Account account;
    private Card card;
    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer(
                1L,
                "João da Silva",
                "joao@email.com",
                "senha123",
                "1985-01-01",
                "111.222.333-44",
                "11999999999",
                List.of()
        );
        account = new Account(
                "123456",
                customer,
                AccountType.CHECKING,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                500.0,
                LocalDate.now()
        );

        card = new Card(
                1L,
                "1111222233334444",
                CardType.DEBIT,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                List.of(),
                account
        );
    }

    @Test
    void shouldCreateDebitCardSuccessfully() {
        CardRequest request = new CardRequest(CardType.DEBIT, "1111222233334444", BigDecimal.ZERO);

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArguments()[0]);

        CardResponse response = cardService.cardCreate(request, "123456");

        assertEquals("1111222233334444", response.cardNumber());
        assertEquals(BigDecimal.valueOf(500.0), response.cardLimit());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void shouldCreateCreditCardSuccessfully() {
        CardRequest request = new CardRequest(CardType.CREDIT, "5555666677778888", BigDecimal.valueOf(2000));

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArguments()[0]);

        CardResponse response = cardService.cardCreate(request, "123456");

        assertEquals("5555666677778888", response.cardNumber());
        assertEquals(BigDecimal.valueOf(2000), response.cardLimit());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundOnCreate() {
        CardRequest request = new CardRequest(CardType.DEBIT, "0000111122223333", BigDecimal.ZERO);
        when(accountRepository.findByAccountNumber("000000")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            cardService.cardCreate(request, "000000");
        });
    }

    @Test
    void shouldReturnCardById() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        CardResponse response = cardService.getCardById(1L);

        assertEquals("1111222233334444", response.cardNumber());
    }

    @Test
    void shouldThrowExceptionWhenCardNotFoundById() {
        when(cardRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardService.getCardById(99L));
    }

    @Test
    void shouldReturnCardsByAccountNumber() {
        when(cardRepository.findAllCardsByAccountAccountNumber("123456")).thenReturn(Optional.of(List.of(card)));

        List<CardResponse> responses = cardService.getAllCardByAccountNumber("123456");

        assertEquals(1, responses.size());
        assertEquals("1111222233334444", responses.get(0).cardNumber());
    }

    @Test
    void shouldUpdateCardLimitSuccessfully() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        CardUpdate update = new CardUpdate(BigDecimal.valueOf(5000));
        CardResponse response = cardService.cardUpdate(update, 1L);

        assertEquals(BigDecimal.valueOf(5000), response.cardLimit());
    }

    @Test
    void shouldDeleteCardById() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        cardService.cardDelete(1L);

        verify(cardRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCard() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardService.cardDelete(2L));
    }
}
