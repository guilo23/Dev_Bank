package com.bia.dev_bank.entity;

import com.bia.dev_bank.entity.enums.CardType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_card")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String cardNumber;

  @Enumerated(EnumType.STRING)
  private CardType cardType;

  @Column(precision = 10, scale = 2)
  private BigDecimal cardLimit;

  @Column(precision = 10, scale = 2)
  private BigDecimal cardBilling;

  @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<CreditPurchase> purchases = new ArrayList<>();

  @OneToMany(mappedBy = "card")
  private List<CardPayments> installments = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "account_number")
  private Account account;
}
