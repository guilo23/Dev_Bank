package com.bia.dev_bank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "tb_transaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private BigDecimal amount;

  @ManyToOne
  @JoinColumn(name = "destiny_account_id")
  private Account destinyAccount;

  @ManyToOne
  @JoinColumn(name = "origin_account_id")
  private Account originAccount;

  @ManyToOne
  @JoinColumn(name = "loan_payment_id")
  private LoanPayments loanPayment;

  @ManyToOne
  @JoinColumn(name = "card_payment_id")
  private CardPayments cardPayment;

  private LocalDate transactionDate;
}
