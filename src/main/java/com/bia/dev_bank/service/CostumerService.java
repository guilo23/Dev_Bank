package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.CostumerRequest;
import com.bia.dev_bank.dto.CostumerResponse;
import com.bia.dev_bank.entity.Costumer;
import com.bia.dev_bank.repository.CostumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CostumerService {
    @Autowired
    private CostumerRepository costumerRepository;

    public CostumerResponse createCostumer(CostumerRequest request){
        var costumer = new Costumer(
                null,
                request.name(),
                request.email(),
                request.password(),
                request.birthday(),
                request.CPF(),
                request.phoneNumber(),
                List.of()
        );
        costumerRepository.save(costumer);
        return new CostumerResponse(costumer.getName());
    }
}
//private Long id;
//
//private String name;
//
//@Column(unique = true)
//private String email;
//
//private String password;
//
//private String birthday;
//
//@Column(unique = true)
//private String CPF;
//
//private String phoneNumber;
//
//@OneToMany(mappedBy = "costumer",
//        cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//private List<Account> accounts;
//
//@Embedded
//private Address address;