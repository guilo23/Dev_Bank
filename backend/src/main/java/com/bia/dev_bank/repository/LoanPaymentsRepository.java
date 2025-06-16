package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.LoanPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanPaymentsRepository extends JpaRepository<LoanPayments, Long> {}
