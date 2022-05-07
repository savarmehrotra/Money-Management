package com.money.management.service;

import com.money.management.controller.AccountInfoRestController;
import com.money.management.exception.AccountNotFoundException;
import com.money.management.exception.InsufficientFundsException;
import com.money.management.model.entity.AccountBalanceEntity;
import com.money.management.model.entity.TransactionEntity;
import com.money.management.model.request.WithdrawalTransactionRequest;
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
 * Class is Responsible to make Withdrawal Transactions
 * Reason why it's kept in a separate class and not merged with Deposits is for extensibility in future
 * Certain use cases like overdraft , allowing customer withdrawal on interest, etc.
 *
 * */
@Service
@RequiredArgsConstructor
public class WithdrawalTransactionService implements TransactionService<WithdrawalTransactionRequest> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountInfoRestController.class);
  @Autowired
  private AccountBalanceRepository accountBalanceRepository;
  @Autowired
  private TransactionRepository transactionRepository;

  @Override
  public Optional<TransactionEntity> executeTransaction(final WithdrawalTransactionRequest withdrawalTransactionRequest)
          throws InsufficientFundsException, AccountNotFoundException {

    Optional<AccountBalanceEntity> accountBalanceEntityInstance = accountBalanceRepository.findByAccountNumber(
            withdrawalTransactionRequest.getAccountNumber());

    if (accountBalanceEntityInstance.isPresent()) {
      if(hasSufficientFunds(accountBalanceEntityInstance.get(), withdrawalTransactionRequest)){

        BigDecimal currentBalance = accountBalanceEntityInstance.get().getCurrentBalance();
        accountBalanceEntityInstance
                .get()
                .setCurrentBalance(currentBalance.subtract(withdrawalTransactionRequest.getAmount()));
        try {
          accountBalanceRepository.save(accountBalanceEntityInstance.get());
          LOGGER.info("Successfully Withdrew From Account : {} ", withdrawalTransactionRequest.getAccountNumber());
          TransactionEntity transactionEntity =
                  TransactionEntity.builder()
                          .sourceAccountId(withdrawalTransactionRequest.getAccountNumber())
                          .destinationAccountId(withdrawalTransactionRequest.getAccountNumber())
                          .transactionAmount(withdrawalTransactionRequest.getAmount())
                          .transactionType(withdrawalTransactionRequest.getTransactionType())
                          .lastUpdated(LocalDateTime.now())
                          .reference(withdrawalTransactionRequest.getReference())
                          .build();
          transactionRepository.save(transactionEntity);
          LOGGER.info("Successfully Saved Transaction for Account : {} ", withdrawalTransactionRequest.getAccountNumber());
          return Optional.of(transactionEntity);
        } catch (Exception ex) {
          //TODO : Add error handling based on partial failures. Or mark this is an atomic update via isolation levels in JPA
        }
      }
      else {
        throw new InsufficientFundsException("Available Balance Only "
                + accountBalanceEntityInstance.get().getCurrentBalance());
      }
    }
    else{
      throw new AccountNotFoundException("Account : " + withdrawalTransactionRequest.getAccountNumber()
              + " Doesn't Exist");
    }
    return Optional.empty();
  }

  private boolean hasSufficientFunds(final AccountBalanceEntity accountBalanceEntity,
      final WithdrawalTransactionRequest withdrawalTransactionRequest) {

    return accountBalanceEntity.getCurrentBalance().compareTo(withdrawalTransactionRequest.getAmount()) > 0;
  }
}