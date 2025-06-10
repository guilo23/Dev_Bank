package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.AccountRequest;
import com.bia.dev_bank.dto.AccountResponse;
import com.bia.dev_bank.dto.AccountUpdate;
import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;


    public AccountResponse createAccount(AccountRequest request,Long customerId){
        var customer = customerRepository.findById(customerId).orElseThrow(
                ()-> new EntityNotFoundException("Não foi encontrado nenhum cliente com o id:  " + customerId));
        var account = new Account(
                generateAccountNumber(),
                customer,
                request.AccountType(),
                List.of(),
                List.of(),
                request.currentBalance(),
                LocalDate.now()
        );
        accountRepository.save(account);
        return new AccountResponse(account.getAccountNumber(),
                                   account.getCustomer().getName(),
                                   account.getAccountType(),
                                   account.getCurrentBalance()
        );
    }
    public AccountResponse getAccountById(String accountNumber){
        var account = accountRepository.findByAccountNumber(accountNumber);

        return  new AccountResponse(account.getAccountNumber(),
                account.getCustomer().getName(),
                account.getAccountType(),
                account.getCurrentBalance()
        );
    }
    public List<AccountResponse> getAllAccountByCostumerId(Long customerId){
        var accounts = accountRepository.findAllAccountByCustomerId(customerId);

        return  accounts.stream().map(AccountResponse::new).collect(Collectors.toList());
    }
    public void accountUpdate(String accountNumber, AccountUpdate update){
        var account = accountRepository.findByAccountNumber(accountNumber);
        accountRepository.save(account);
    }
    public void accountUpdate(String accountNumber){
        var account = accountRepository.findByAccountNumber(accountNumber);
        accountRepository.delete(account);
    }

    private String generateAccountNumber() {
        return String.format("%08d", new Random().nextInt(100_000_000));
    }





















//    public class ContaGenerator {
//
//        public static String gerarNumeroConta(int sequencia) {
//            String conta = String.format("%08d", sequencia); // Ex: "00000001"
//            int dv = calcularDigitoVerificador(conta);
//            return conta + "-" + dv;
//        }
//
//        private static int calcularDigitoVerificador(String conta) {
//            int[] pesos = {9, 8, 7, 6, 5, 4, 3, 2}; // pesos de trás pra frente
//            int soma = 0;
//
//            for (int i = 0; i < conta.length(); i++) {
//                int digito = Character.getNumericValue(conta.charAt(i));
//                soma += digito * pesos[i];
//            }
//
//            int resto = soma % 11;
//            if (resto < 2) return 0;
//            return 11 - resto;
//        }
//
//        public static void main(String[] args) {
//            for (int i = 1; i <= 10; i++) {
//                System.out.println(gerarNumeroConta(i));
//            }
//        }
//    }


}
