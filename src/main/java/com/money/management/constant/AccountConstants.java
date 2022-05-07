package com.money.management.constant;

import java.util.regex.Pattern;

public class AccountConstants {

    public static final String INVALID_SEARCH_CRITERIA =
            "The provided sort code or account number did not match the expected format";

    public static final String NO_ACCOUNT_FOUND = "No Account Found With Account Number : ";

    public static final String INSUFFICIENT_FUNDS_IN_ACCOUNT = "Insufficient Funds in the Account : ";

    public static final String FUND_WITHDRAWAL_FAILURE = "Unable to Withdraw Funds From Account";

    public static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^[0-9]{5}$");

    public static final String INVALID_ACCOUNT_WITHDRAWAL_REQUEST = "InvalidAccountInvalidRequest";
}