package com.money.management.model.entity;

import com.money.management.model.enums.TransactionType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// TODO Add support for Bank charges, currency conversion, setup repeat payment/ standing order
@Entity
@Table(name = "Transactions", schema = "Bank")
@SequenceGenerator(name = "transaction_id_generator", sequenceName = "transaction_sequence",
    schema = "Bank",
    initialValue = 10)
@EqualsAndHashCode(of = {"transactionId"})
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_generator")
  private long transactionId;
  private String sourceAccountId;
  private String destinationAccountId;
  private BigDecimal transactionAmount;

  //TODO : Evaluate use in sync flow.
  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;
  private LocalDateTime lastUpdated;
  private String reference;
}