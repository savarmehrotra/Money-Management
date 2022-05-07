
package com.money.management.service;

import com.money.management.model.entity.AccountBalanceEntity;
import com.money.management.model.enums.TransactionType;
import com.money.management.model.request.WireTransferTransactionRequest;
import com.money.management.exception.AccountNotFoundException;
import com.money.management.exception.InsufficientFundsException;
import com.money.management.model.entity.TransactionEntity;
import com.money.management.repositories.AccountBalanceRepository;
import com.money.management.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WireTransferTransactionService implements TransactionService<WireTransferTransactionRequest> {

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Optional<TransactionEntity> executeTransaction(final WireTransferTransactionRequest wireTransferTransactionRequest)
            throws InsufficientFundsException, AccountNotFoundException {

    Optional<AccountBalanceEntity> sourceAccountBalanceInstance =
        accountBalanceRepository.findByAccountNumber(
            wireTransferTransactionRequest.getSourceAccountNumber());

    Optional<AccountBalanceEntity> targetAccountBalanceInstance =
        accountBalanceRepository.findByAccountNumber(
            wireTransferTransactionRequest.getDestinationAccountNumber());

    if (!(sourceAccountBalanceInstance.isPresent() || targetAccountBalanceInstance.isPresent())) {
      throw new AccountNotFoundException("Account Not Found");
    }

    if ((sourceAccountBalanceInstance.get().getCurrentBalance())
            .compareTo(wireTransferTransactionRequest.getAmount()) < 0) {
      throw new InsufficientFundsException(
          "Insufficient Funds in Source Account : " + wireTransferTransactionRequest.getAmount() + "To Make Transfer");
    }

    try {
      TransactionEntity transactionEntity = TransactionEntity.builder()
              .sourceAccountId(wireTransferTransactionRequest.getSourceAccountNumber())
              .destinationAccountId(wireTransferTransactionRequest.getDestinationAccountNumber())
              .transactionAmount(wireTransferTransactionRequest.getAmount())
              .transactionType(TransactionType.TRANSFER)
              .lastUpdated(LocalDateTime.now())
              .reference(wireTransferTransactionRequest.getReference())
              .build();
      transactionRepository.save(transactionEntity);

      BigDecimal sourceAccountCurrentBalance =
          sourceAccountBalanceInstance.get().getCurrentBalance();
      sourceAccountBalanceInstance
          .get()
          .setCurrentBalance(
              sourceAccountCurrentBalance.subtract(wireTransferTransactionRequest.getAmount()));
      accountBalanceRepository.save(sourceAccountBalanceInstance.get());

      BigDecimal targetAccountCurrentBalance =
          targetAccountBalanceInstance.get().getCurrentBalance();
      targetAccountBalanceInstance
          .get()
          .setCurrentBalance(
              targetAccountCurrentBalance.subtract(wireTransferTransactionRequest.getAmount()));
      accountBalanceRepository.save(targetAccountBalanceInstance.get());
      return Optional.of(transactionEntity);
    } catch (Exception ex) {
      // TODO : Handling For Partial Failures. Atomicity for all 3 updates.
    }
    return Optional.empty();
  }
}