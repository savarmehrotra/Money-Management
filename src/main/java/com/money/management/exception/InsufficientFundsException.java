package com.money.management.exception;

public class InsufficientFundsException extends Exception {

  public InsufficientFundsException(final String message) {
    super(message);
  }
}