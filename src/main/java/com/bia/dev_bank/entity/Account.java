package com.bia.dev_bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String AccountNumber;

    @ManyToOne
    @JoinColumn(name="costumer_id")
    private Customer costumer;

    private String AccountType;

    @OneToMany(mappedBy = "originAccount")
    private List<Transaction> transactionsSent;

    @OneToMany(mappedBy = "destinyAccount")
    private List<Transaction> transactionsReceived;

    private double currentBalance;

    private Date dateOpened;

    private Date dateClosed;

    private String accountStatus;

}
