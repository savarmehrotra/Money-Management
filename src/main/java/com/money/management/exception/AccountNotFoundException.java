package com.money.management.exception;

public class AccountNotFoundException extends Exception {

  public AccountNotFoundException(final String message) {
    super(message);
  }
}