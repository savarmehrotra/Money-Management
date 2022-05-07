package com.money.management.utils;

import com.money.management.constant.AccountConstants;
import java.math.BigDecimal;

public class RequestValidator {

    public static boolean isAmountValid(final BigDecimal amount) {
        return amount.compareTo(new BigDecimal("0.0")) > 0;
    }

    public static boolean isAccountNumberFormatValid(final String accountNumber) {
        return AccountConstants.ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).find();
    }
}