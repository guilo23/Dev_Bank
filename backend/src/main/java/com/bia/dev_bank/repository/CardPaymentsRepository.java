package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

@Repository
public interface CardPaymentsRepository extends JpaRepository<CardPayments, Long> {
  List<CardPayments> findByCardId(Long id);

  List<CardPayments> findAllByCardId(Long id);

  @Query(
      """
          SELECT cp FROM CardPayments cp
          WHERE cp.card.id = :cardId
            AND CAST(cp.dueDate AS string) LIKE :month%
            AND cp.paid = "NOT_PAYED"
          ORDER BY cp.dueDate DESC
          """)
  Optional<List<CardPayments>> findByCardIdAndMonth(
      @Param("cardId") Long cardId, @Param("month") String month);
}
