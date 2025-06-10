package com.bia.dev_bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_account")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    private String accountType;

    @OneToMany(mappedBy = "originAccount",cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private List<Transaction> transactionsSent;

    @OneToMany(mappedBy = "destinyAccount",cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private List<Transaction> transactionsReceived;

    private double currentBalance;

      private LocalDate dateOpened;

}
