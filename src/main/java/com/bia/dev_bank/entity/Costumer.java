package com.bia.dev_bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tb_costumer")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Costumer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String birthday;

    @Column(unique = true)
    private String CPF;

    private String phoneNumber;

    @OneToMany(mappedBy = "costumer",
            cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Account> accounts;

}
