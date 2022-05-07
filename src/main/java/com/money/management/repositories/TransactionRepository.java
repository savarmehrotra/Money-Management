package com.money.management.repositories;

import com.money.management.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

  List<TransactionEntity> findBySourceAccountIdOrderByLastUpdated(final String sourceAccountId);

  List<TransactionEntity> findByDestinationAccountIdOrderByLastUpdated(final String destinationAccountId);
}