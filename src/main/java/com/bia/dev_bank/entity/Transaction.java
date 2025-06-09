package com.bia.dev_bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tb_transaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String type;

    @ManyToOne
    @JoinColumn(name = "destiny_account_id")
    private Account destinyAccount;

    @ManyToOne
    @JoinColumn(name = "origin_account_id")
    private Account originAccount;

    private LocalDate transactionDate;

}
