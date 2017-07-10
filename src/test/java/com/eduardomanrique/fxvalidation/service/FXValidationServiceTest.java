package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.entity.Currency;
import com.eduardomanrique.fxvalidation.products.CurrencyPair;
import com.eduardomanrique.fxvalidation.products.FXTransaction;
import com.eduardomanrique.fxvalidation.products.Forward;
import com.eduardomanrique.fxvalidation.products.Spot;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test_service")
public class FXValidationServiceTest {

    @Autowired
    FXValidationService fxValidationService;

    @Test
    public void testValidateSpot() {
        Spot transaction = fillTransaction(new Spot());

        List<FXValidationService.FXValidationResult> result =
                fxValidationService.validateTransaction(Collections.singletonList(transaction));
        List<Validation.ErrorMessage> messages = result.get(0).getErrorMessages();
        Assert.assertTrue("Should have 1 messages", messages.size() == 1);
        Assert.assertTrue("Should have SpotRule message", messages.get(0).getCode().equals("SpotRule"));
    }

    @Test
    public void testValidateNonSpot() {
        Forward transaction = fillTransaction(new Forward());
        List<FXValidationService.FXValidationResult> result =
                fxValidationService.validateTransaction(Collections.singletonList(transaction));
        List<Validation.ErrorMessage> messages = result.get(0).getErrorMessages();
        Assert.assertTrue("Should have 1 messages", messages.size() == 1);
        Assert.assertTrue("Should have NonSpotRule message", messages.get(0).getCode().equals("NonSpotRule"));
    }

    private <T> T fillTransaction(FXTransaction transaction) {
        transaction.setCustomer("Customer");
        CurrencyPair currencyPair = new CurrencyPair();
        Currency ccy1 = new Currency();
        ccy1.setIsoCode("ABC");
        currencyPair.setCurrency1(Optional.of(ccy1));
        Currency ccy2 = new Currency();
        ccy2.setIsoCode("DEF");
        currencyPair.setCurrency2(Optional.of(ccy2));
        transaction.setCurrencyPair(currencyPair);
        transaction.setDirection(FXTransaction.Direction.BUY);
        transaction.setTradeDate(new Date());
        transaction.setCustomer("Customer");
        return (T) transaction;
    }
}