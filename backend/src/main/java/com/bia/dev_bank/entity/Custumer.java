package com.bia.dev_bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tb_costumer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Custumer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String customerType;

}
