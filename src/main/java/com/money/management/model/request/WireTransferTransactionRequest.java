package com.money.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WireTransferTransactionRequest extends TransactionRequest{

    @NonNull
    private String sourceAccountNumber;

    @NonNull
    private String destinationAccountNumber;
}