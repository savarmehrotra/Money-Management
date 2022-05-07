package com.money.management.controller;

import com.money.management.constant.AccountConstants;
import com.money.management.exception.AccountNotFoundException;
import com.money.management.model.entity.TransactionEntity;
import com.money.management.model.request.DepositTransactionRequest;
import com.money.management.service.DepositTransactionService;
import com.money.management.utils.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/account-deposit")
public class AccountDepositController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountInfoRestController.class);
  private final DepositTransactionService depositTransactionService;

  @PostMapping(
      value = "/",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity makeAccountDeposit(
      @RequestBody final DepositTransactionRequest depositTransactionRequest) {

    LOGGER.info(
        "Account Deposit Request Received For Account : {}, Amount : {}",
        depositTransactionRequest.getAccountNumber(),
        depositTransactionRequest.getAmount());

    if (!(RequestValidator.isAmountValid(depositTransactionRequest.getAmount())
        && RequestValidator.isAccountNumberFormatValid(
            depositTransactionRequest.getAccountNumber()))) {
      LOGGER.error(
          "Invalid Account Deposit Request Received For Account : {}, Amount : {}",
          depositTransactionRequest.getAccountNumber(),
          depositTransactionRequest.getAmount());
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body(AccountConstants.INVALID_ACCOUNT_WITHDRAWAL_REQUEST);
    }

    try {
      Optional<TransactionEntity> transactionEntityInstance =
          depositTransactionService.executeTransaction(
              DepositTransactionRequest.builder()
                  .accountNumber(depositTransactionRequest.getAccountNumber())
                  .amount(depositTransactionRequest.getAmount())
                  .reference(depositTransactionRequest.getReference())
                  .build());

      return transactionEntityInstance.<ResponseEntity>map(
              transactionEntity -> ResponseEntity.status(HttpStatus.OK)
                      .body("TransactionId : " + transactionEntity.getTransactionId()))
              .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body(AccountConstants.FUND_WITHDRAWAL_FAILURE));
    } catch (AccountNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(AccountConstants.NO_ACCOUNT_FOUND + depositTransactionRequest.getAccountNumber());
    }
  }
}