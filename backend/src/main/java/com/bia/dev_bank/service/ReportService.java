package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.report.StatementResponse;
import com.bia.dev_bank.entity.enums.CardType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
  private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
  private final TransactionService transactionService;
  private final CardService cardService;

  public List<StatementResponse> allcardReports(String accountNumber) {
    logger.info("Generating all reports for account {}", accountNumber);
    List<StatementResponse> reports = new ArrayList<>();
    var cards = cardService.getAllCardForReport(accountNumber);
    logger.info("Found {} cards for account {}", cards.size(), accountNumber);

    for (var card : cards) {
      if (card.getCardType().equals(CardType.DEBIT)) {
        logger.info("Generating debit report for card {}", card.getCardNumber());
        var debitList = cardService.cardsDebitPaymentsReport(card.getCardNumber());
        reports.addAll(debitList);
      } else {
        logger.info("Generating credit report for card {}", card.getCardNumber());
        var creditList = cardService.cardsCreditPaymentsReport(card.getCardNumber());
        reports.addAll(creditList);
      }
    }
    var transactions = transactionService.getStatementByAccountNumber(accountNumber);
    reports.addAll(transactions);
    logger.info("Generated all reports for account {}", accountNumber);
    return reports;
  }
}
