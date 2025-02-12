package com.bia.dev_bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tb_account")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String AccountNumber;

    private String AccountType;

    private double currentBalance;

    private Date dateOpened;

    private Date dateClosed;

    private String accountStatus;

}
