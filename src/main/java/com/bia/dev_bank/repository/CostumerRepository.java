package com.bia.dev_bank.repository;

import com.bia.dev_bank.entity.Costumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostumerRepository extends JpaRepository<Costumer,Long> {
}
