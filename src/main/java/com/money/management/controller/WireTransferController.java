package com.money.management.controller;

import com.money.management.constant.AccountConstants;
import com.money.management.constant.TransactionConstants;
import com.money.management.exception.AccountNotFoundException;
import com.money.management.exception.InsufficientFundsException;
import com.money.management.model.entity.TransactionEntity;
import com.money.management.model.request.WireTransferTransactionRequest;
import com.money.management.service.WireTransferTransactionService;
import com.money.management.utils.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class WireTransferController {

  private static final Logger LOGGER = LoggerFactory.getLogger(WireTransferController.class);

  private final WireTransferTransactionService wireTransferTransactionService;

  @PostMapping(value = "/transfer",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> makeTransfer(@RequestBody WireTransferTransactionRequest wireTransferTransactionRequest) {

      LOGGER.info("Wire Transfer Request Received for Accounts {}, {}",
              wireTransferTransactionRequest.getSourceAccountNumber(), wireTransferTransactionRequest.getDestinationAccountNumber());
    if (!(RequestValidator.isAmountValid(wireTransferTransactionRequest.getAmount())
        || RequestValidator.isAccountNumberFormatValid(wireTransferTransactionRequest.getSourceAccountNumber())
        || RequestValidator.isAccountNumberFormatValid(wireTransferTransactionRequest.getDestinationAccountNumber()))) {
        LOGGER.error("Wire Transfer Request is Invalid");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TransactionConstants.INVALID_TRANSACTION);
    }
    try {
      Optional<TransactionEntity> transactionEntityInstance =
          wireTransferTransactionService.executeTransaction(
              WireTransferTransactionRequest.builder()
                  .amount(wireTransferTransactionRequest.getAmount())
                  .sourceAccountNumber(wireTransferTransactionRequest.getSourceAccountNumber())
                  .destinationAccountNumber(
                      wireTransferTransactionRequest.getDestinationAccountNumber())
                  .reference(wireTransferTransactionRequest.getReference())
                  .build());
        LOGGER.error("Successfully Executed Transaction");
        return new ResponseEntity<>(transactionEntityInstance.get(), HttpStatus.OK);
    } catch (InsufficientFundsException insufficientFundsException) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(AccountConstants.INSUFFICIENT_FUNDS_IN_ACCOUNT + wireTransferTransactionRequest.getAmount());
    } catch (AccountNotFoundException accountNotFoundException) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AccountConstants.NO_ACCOUNT_FOUND);
    }
  }
}