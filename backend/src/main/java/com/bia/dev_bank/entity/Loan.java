package com.bia.dev_bank.entity;

import com.bia.dev_bank.entity.enums.LoanType;
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
@Table(name = "tb_loan")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    private LocalDate startDate;

    private BigDecimal loanAmount;

    private int installments;

    private BigDecimal monthlyInstallment;

    private BigDecimal totalPayable;

    private BigDecimal interestRate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanPayments> loanPayments = new ArrayList<>();

    public void calculatedLoanDetails(){
        BigDecimal totalInterest = loanAmount.multiply(interestRate);
        this.totalPayable = loanAmount.add(totalInterest);
        this.monthlyInstallment = totalPayable.divide(new BigDecimal(installments));
    }



}
