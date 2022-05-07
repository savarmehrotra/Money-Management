package com.money.management.controller;

import com.money.management.model.entity.TransactionEntity;
import com.money.management.model.request.AccountInfoRequest;
import com.money.management.model.entity.AccountBalanceEntity;
import com.money.management.service.AccountInfoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.money.management.constant.AccountConstants.*;

@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountInfoRestController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountInfoRestController.class);
  private final AccountInfoService accountInfoService;

  @GetMapping(value = "/balance",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> checkAccountBalance(@RequestBody AccountInfoRequest accountInfoRequest) {
    LOGGER.info("Account Information Requested For Account" + accountInfoRequest.getAccountNumber());
    Optional<AccountBalanceEntity> accountBalanceInstance =
        accountInfoService.getAccountBalance(accountInfoRequest.getAccountNumber());

    if (accountBalanceInstance.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK)
              .body(accountBalanceInstance.get());
    } else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(NO_ACCOUNT_FOUND);
    }
  }

  @GetMapping(value = "/transactions",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getAccountTransactions(@RequestBody AccountInfoRequest accountInfoRequest){
    LOGGER.info("Transactions Requested For Account : " + accountInfoRequest.getAccountNumber());
    List<TransactionEntity> sourceTransactions =
            accountInfoService.getTransactionsForAccountAsSource(accountInfoRequest.getAccountNumber());
    List<TransactionEntity> destinationTransactions =
            accountInfoService.getTransactionsForAccountAsDestination(accountInfoRequest.getAccountNumber());
    HashMap<String, List<TransactionEntity>> transactionsMap = new HashMap<>();
    transactionsMap.put("Account As Destination", destinationTransactions);
    transactionsMap.put("Account As Source", sourceTransactions);

    return ResponseEntity.status(HttpStatus.OK)
            .body(transactionsMap);
  }
}