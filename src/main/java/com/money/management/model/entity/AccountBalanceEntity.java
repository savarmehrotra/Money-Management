package com.money.management.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

// TODO Add support for foreign currency accounts
@Entity
@Table(name = "Account_Balances", schema = "Bank")
@EqualsAndHashCode(of = {"accountNumber"})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceEntity {

  @Id
  private String accountNumber;

  private String accountHolderName;

  private BigDecimal currentBalance;

  private String currency;
}