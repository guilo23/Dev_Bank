package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.CardPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardPaymentsRepository extends JpaRepository<CardPayments,Long>{
    List<CardPayments> findByCardId(Long id);
}
