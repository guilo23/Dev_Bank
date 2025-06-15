package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.CardPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardPaymentsRepository extends JpaRepository<CardPayments,Long> {
}
