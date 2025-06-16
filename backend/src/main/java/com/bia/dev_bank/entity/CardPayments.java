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
@Table(name = "tb_cardPayments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CardPayments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String productName;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal installmentAmount;

    private int installmentNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalBuying;

    @Column(precision = 10, scale = 2)
    private BigDecimal paidAmount;

    private LocalDate dueDate;

    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    private PayedStatus PAID;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @OneToMany(mappedBy = "cardPayment", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public void calculatedPaymentsDetails(){
        this.installmentAmount = totalBuying.divide(
                new BigDecimal(installmentNumber),2);
    }



}

