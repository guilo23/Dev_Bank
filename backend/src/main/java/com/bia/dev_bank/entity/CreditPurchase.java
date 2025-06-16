package com.bia.dev_bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "tb_creditPhurcase")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreditPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private BigDecimal amount;
    private LocalDate purchaseDate;
    private Integer installmentNumber;

    @ManyToOne
    private Card card;
}
