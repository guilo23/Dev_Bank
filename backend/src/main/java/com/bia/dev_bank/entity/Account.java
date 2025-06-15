package com.bia.dev_bank.entity;

import com.bia.dev_bank.entity.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @OneToMany(mappedBy = "originAccount",cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private List<Transaction> transactionsSent;

    @OneToMany(mappedBy = "destinyAccount",cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private List<Transaction> transactionsReceived;

    @OneToMany(mappedBy = "account")
    private List<Card> cards;

    private BigDecimal currentBalance;

    private LocalDate dateOpened;

}
