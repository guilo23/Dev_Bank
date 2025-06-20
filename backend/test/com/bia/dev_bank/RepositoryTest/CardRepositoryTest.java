package com.bia.dev_bank.RepositoryTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Card;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.repository.CardRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class CardRepositoryTest {
  @Autowired private CardRepository cardRepository;
  private Account account;

  @Test
  void shouldCreateandFindCard() {
    var card =
        new Card(
            null,
            "1234",
            CardType.DEBIT,
            BigDecimal.ONE,
            BigDecimal.ONE,
            new ArrayList<>(),
            new ArrayList<>(),
            account);
    var saved = cardRepository.save(card);
    assertNotNull(saved.getId());
    assertNotNull(saved.getCardType());
  }

  @Test
  void ShouldDeleteCard() {
    var card =
        new Card(
            null,
            "1234",
            CardType.DEBIT,
            BigDecimal.ONE,
            BigDecimal.ONE,
            new ArrayList<>(),
            new ArrayList<>(),
            account);
    var saved = cardRepository.save(card);
    cardRepository.delete(saved);
    assertTrue(!cardRepository.existsById(saved.getId()));
  }
}
