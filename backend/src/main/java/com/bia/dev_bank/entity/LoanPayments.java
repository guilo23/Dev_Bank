package com.bia.dev_bank.entity;

import com.bia.dev_bank.entity.enums.PayedStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_loan_payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanPayments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanPaymentId;

    @Column(nullable = false)
    private LocalDate scheduledPaymentDate;

    @Enumerated(EnumType.STRING)
    private PayedStatus payedStatus;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal principalAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal interestAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal paidAmount;

    private LocalDate paidDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @OneToMany(mappedBy = "loanPayment", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();


}
