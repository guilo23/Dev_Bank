package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.Card;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
  Optional<List<Card>> findAllCardsByAccountAccountNumber(String accountNumber);

  Optional<Card> findCardByCardNumber(String accountNumber);
}
