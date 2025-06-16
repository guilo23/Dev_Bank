package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
  Optional<Account> findByAccountNumber(String accountNumber);

  List<Account> findAllAccountByCustomerId(Long id);
}
