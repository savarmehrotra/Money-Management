package com.money.management.service;

import com.money.management.controller.AccountInfoRestController;
import com.money.management.exception.AccountNotFoundException;
import com.money.management.model.entity.AccountBalanceEntity;
import com.money.management.model.entity.TransactionEntity;
import com.money.management.model.request.DepositTransactionRequest;
import com.money.management.repositories.AccountBalanceRepository;
import com.money.management.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This Class is Responsible to Make Deposits For a Particular Account. In this process it :
 * 1)Updates the Account Balance
 * 2) Makes a Deposit or Withdrawal Entry Into the List of Transactionsfor that Account
 * TODO : 3) Cross Currency Deposits
 */
@Service
@RequiredArgsConstructor
public class DepositTransactionService implements TransactionService<DepositTransactionRequest> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountInfoRestController.class);
  @Autowired
  private AccountBalanceRepository accountBalanceRepository;
  @Autowired
  private TransactionRepository transactionRepository;

  @Override
  public Optional<TransactionEntity> executeTransaction(
      final DepositTransactionRequest depositTransactionRequest) throws AccountNotFoundException {
    Optional<AccountBalanceEntity> accountBalanceEntityInstance =
        accountBalanceRepository.findByAccountNumber(depositTransactionRequest.getAccountNumber());

    if (accountBalanceEntityInstance.isPresent()) {
      BigDecimal currentBalance = accountBalanceEntityInstance.get().getCurrentBalance();
      accountBalanceEntityInstance
          .get()
          .setCurrentBalance(currentBalance.add(depositTransactionRequest.getAmount()));
      try {
        accountBalanceRepository.save(accountBalanceEntityInstance.get());
        LOGGER.info("Successfully Deposited Into Account : {} ", depositTransactionRequest.getAccountNumber());
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .sourceAccountId(depositTransactionRequest.getAccountNumber())
                .transactionAmount(depositTransactionRequest.getAmount())
                .transactionType(depositTransactionRequest.getTransactionType())
                .lastUpdated(LocalDateTime.now())
                .reference(depositTransactionRequest.getReference())
                .build();
        transactionRepository.save(transactionEntity);
        LOGGER.info("Successfully Saved Transaction Deposit Transaction For Account : {} ",
            depositTransactionRequest.getAccountNumber());
        return Optional.of(transactionEntity);
      } catch (Exception ex) {
        // TODO : Add error handling based on partial failures. Or mark this is an atomic update via isolation levels in JPA
      }
    } else {
      throw new AccountNotFoundException("Account : " + depositTransactionRequest.getAccountNumber() + " Doesn't Exist");
    }
    return Optional.empty();
  }
}