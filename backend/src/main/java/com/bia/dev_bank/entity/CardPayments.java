package com.bia.dev_bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_cardPayments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CardPayments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal installmentAmount;

    private int installmentNumber;

    private int totalInstallments;

    private LocalDate dueDate;

    private boolean paid;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
}
