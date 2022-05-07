package com.money.management.controller;

import com.money.management.constant.AccountConstants;
import com.money.management.exception.AccountNotFoundException;
import com.money.management.exception.InsufficientFundsException;
import com.money.management.model.entity.TransactionEntity;
import com.money.management.model.request.WithdrawalTransactionRequest;
import com.money.management.service.WithdrawalTransactionService;
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
@RequestMapping("api/v1/account-withdrawal")
public class AccountWithdrawalController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountInfoRestController.class);
  private final WithdrawalTransactionService withdrawalTransactionService;

  @PostMapping(value = "/",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity makeAccountWithdrawal(@RequestBody final WithdrawalTransactionRequest withdrawalTransactionRequest) {

    LOGGER.info("Account Withdrawal Request Received For Account : {}, Amount : {}",
        withdrawalTransactionRequest.getAccountNumber(), withdrawalTransactionRequest.getAmount());

    if (!(RequestValidator.isAmountValid(withdrawalTransactionRequest.getAmount())
        && RequestValidator.isAccountNumberFormatValid(
            withdrawalTransactionRequest.getAccountNumber()))) {
      LOGGER.error("Invalid Account Withdrawal Request Received For Account : {}, Amount : {}",
          withdrawalTransactionRequest.getAccountNumber(), withdrawalTransactionRequest.getAmount());
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body(AccountConstants.INVALID_ACCOUNT_WITHDRAWAL_REQUEST);
    }

    try {
      Optional<TransactionEntity> transactionEntityInstance = withdrawalTransactionService.executeTransaction(
              WithdrawalTransactionRequest.builder()
                  .accountNumber(withdrawalTransactionRequest.getAccountNumber())
                  .amount(withdrawalTransactionRequest.getAmount())
                  .reference(withdrawalTransactionRequest.getReference())
                  .build());

      return transactionEntityInstance.<ResponseEntity>map(
              transactionEntity -> ResponseEntity.status(HttpStatus.OK)
                      .body("TransactionId : " + transactionEntity.getTransactionId()))
          .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body(AccountConstants.FUND_WITHDRAWAL_FAILURE));
    } catch (InsufficientFundsException ex) {
      return ResponseEntity.status(HttpStatus.OK).body(
              AccountConstants.INSUFFICIENT_FUNDS_IN_ACCOUNT
                  + withdrawalTransactionRequest.getAccountNumber());
    } catch (AccountNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
              AccountConstants.NO_ACCOUNT_FOUND
                  + withdrawalTransactionRequest.getAccountNumber());
    }
  }
}