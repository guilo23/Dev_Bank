package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<Transaction> findTransactionsByOriginAccountAccountNumber(String accountNumber);
}
