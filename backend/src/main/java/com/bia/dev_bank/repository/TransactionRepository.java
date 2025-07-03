package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<Transaction> findTransactionsByOriginAccountAccountNumber(String accountNumber);

  List<Transaction> findByOriginAccountAccountNumberOrderByTransactionDateDesc(String accountNumber);

  List<Transaction> findByDestinyAccountAccountNumberOrderByTransactionDateDesc(String accountNumber);

  @Query("""
    SELECT t FROM Transaction t
    WHERE t.originAccount.accountNumber = :accountNumber
       OR t.destinyAccount.accountNumber = :accountNumber
    ORDER BY t.transactionDate DESC
""")
  Page<Transaction> findByAccountNumber(
          @Param("accountNumber") String accountNumber,
          Pageable pageable
  );

}
