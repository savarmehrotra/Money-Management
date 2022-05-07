package com.money.management.service;

import com.money.management.exception.AccountNotFoundException;
import com.money.management.exception.InsufficientFundsException;
import com.money.management.model.entity.TransactionEntity;
import com.money.management.model.request.TransactionRequest;

import java.util.Optional;

public interface TransactionService<I extends TransactionRequest> {

  public Optional<TransactionEntity> executeTransaction(I transactionRequest) throws InsufficientFundsException, AccountNotFoundException;
}