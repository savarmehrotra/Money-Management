package com.money.management.service;

import com.money.management.model.entity.AccountBalanceEntity;
import com.money.management.model.entity.TransactionEntity;
import com.money.management.repositories.AccountBalanceRepository;
import com.money.management.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
* Class Responsible to provide information on an Account's :
* 1) Balance
* 2) List of Transactions
* */
@Service
@RequiredArgsConstructor
public class AccountInfoService {

  @Autowired
  private AccountBalanceRepository accountBalanceRepository;
  @Autowired
  private TransactionRepository transactionRepository;

  public Optional<AccountBalanceEntity> getAccountBalance(final String accountNumber) {
    return accountBalanceRepository.findByAccountNumber(accountNumber);
  }

  public List<TransactionEntity> getTransactionsForAccountAsSource(final String accountNumber) {
    return transactionRepository.findBySourceAccountIdOrderByLastUpdated(accountNumber);
  }

  public List<TransactionEntity> getTransactionsForAccountAsDestination(final String accountNumber) {
    return transactionRepository.findByDestinationAccountIdOrderByLastUpdated(accountNumber);
  }
}