package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {
    Account findByAccountNumber(String accountNumber);
    List<Account> findAllAccountByCustomerId(Long id);
}
