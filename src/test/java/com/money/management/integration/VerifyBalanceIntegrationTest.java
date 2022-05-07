package com.money.management.integration;

import com.money.management.controller.AccountInfoRestController;
import com.money.management.model.entity.AccountBalanceEntity;
import com.money.management.model.request.AccountInfoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "local")
class VerifyBalanceIntegrationTest {

    @Autowired
    private AccountInfoRestController accountInfoRestController;

    @Test
    void WHEN_ValidAccountDetailsAreGiven_THEN_AccountInfoController_RETURNS_CorrectBalance() {

        var sampleRequest = new AccountInfoRequest();
        sampleRequest.setAccountNumber("10001");

        var body = accountInfoRestController.checkAccountBalance(sampleRequest).getBody();
        var account = (AccountBalanceEntity) body;
        assertThat(account).isNotNull();
        assertThat(account.getAccountHolderName()).isEqualTo("Jane Doe");
        assertThat(account.getAccountNumber()).isEqualTo("10001");
    }
}