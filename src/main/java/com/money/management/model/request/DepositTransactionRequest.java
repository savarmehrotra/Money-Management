package com.money.management.model.request;

import com.money.management.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DepositTransactionRequest extends TransactionRequest {

  @NonNull
  private String accountNumber;
  private final TransactionType transactionType = TransactionType.DEPOSIT;
}