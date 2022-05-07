package com.money.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoRequest {

  @NotBlank(message = "Account number is mandatory")
  private String accountNumber;
}