package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.report.StatementResponse;
import com.bia.dev_bank.entity.enums.CardType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
  private final TransactionService transactionService;
  private final CardService cardService;

  public List<StatementResponse> allcardReports(String accountNumber) {
    List<StatementResponse> reports = new ArrayList<>();
    var cards = cardService.getAllCardForReport(accountNumber);

    for (var card : cards) {
      if (card.getCardType().equals(CardType.DEBIT)) {
        var debitList = cardService.cardsDebitPaymentsReport(card.getCardNumber());
        reports.addAll(debitList);
      } else {
        var creditList = cardService.cardsCreditPaymentsReport(card.getCardNumber());
        reports.addAll(creditList);
      }
    }
    var transactions = transactionService.getStatementByAccountNumber(accountNumber);
    reports.addAll(transactions);
    return reports;
  }
}
