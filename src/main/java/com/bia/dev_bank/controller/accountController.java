package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.AccountRequest;
import com.bia.dev_bank.dto.CostumerRequest;
import com.bia.dev_bank.service.CostumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class accountController {

    @Autowired
    private CostumerService costumerService;

    @PostMapping
    public ResponseEntity createCostumer(@RequestBody CostumerRequest dados){
            var costumer = costumerService.createCostumer(dados);
            return ResponseEntity.ok().body("Parabéns " + costumer.name() +
                    " seu cadastrado foi feito com sucesso agora voce é uma cliente da BIA");
    }
}
