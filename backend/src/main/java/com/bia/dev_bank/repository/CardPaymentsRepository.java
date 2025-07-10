package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.CardPayments;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardPaymentsRepository extends JpaRepository<CardPayments, Long> {
  List<CardPayments> findByCardId(Long id);

  List<CardPayments> findAllByCardId(Long id);
}
