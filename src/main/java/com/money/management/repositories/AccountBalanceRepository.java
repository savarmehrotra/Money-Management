package com.money.management.repositories;

import com.money.management.model.entity.AccountBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountBalanceRepository extends JpaRepository<AccountBalanceEntity, Long> {

    Optional<AccountBalanceEntity> findByAccountNumber(final String accountNumber);
}