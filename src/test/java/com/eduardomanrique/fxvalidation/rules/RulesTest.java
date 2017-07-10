package com.eduardomanrique.fxvalidation.rules;

import com.eduardomanrique.fxvalidation.entity.Currency;
import com.eduardomanrique.fxvalidation.entity.Customer;
import com.eduardomanrique.fxvalidation.products.*;
import com.eduardomanrique.fxvalidation.rulesengine.RuleEngine;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import com.eduardomanrique.fxvalidation.service.FixerIOApiGatewayService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class RulesTest {

    @Autowired
    RuleEngine ruleEngine;

    @Autowired
    FixerIOApiGatewayService fixerIOApiGatewayService;

    @Before
    public void before() {
        when(fixerIOApiGatewayService.businessDaysBetween(any(), any())).thenReturn(2);
        when(fixerIOApiGatewayService.isBusinessDay(any())).thenReturn(true);
    }

    @Test
    public void testInvalidTradeAndValueDate() {
        when(fixerIOApiGatewayService.isBusinessDay(any())).thenReturn(false);
        Validation validation = new Validation() {
        };
        Spot spot = (Spot) fillObjects(new Spot());
        Calendar cal = Calendar.getInstance();
        spot.setValueDate(cal.getTime());
        cal.add(Calendar.DATE, 10);
        spot.setTradeDate(cal.getTime());

        spot.getCustomerEntity().setId(1l);
        spot.getCurrencyPair().getCurrency1().setName("cc1");
        spot.getCurrencyPair().getCurrency2().setName("cc2");

        ruleEngine.fireRules(spot, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 2 messages", messages.size() == 2);
        Assert.assertTrue("ValueDateBeforeTradeDate", messages.get(0).getCode().equals("ValueDateBeforeTradeDate"));
        Assert.assertTrue("ValueDateNotBusinessDate", messages.get(1).getCode().equals("ValueDateNotBusinessDate"));
    }

    @Test
    public void testValidTradeAndValueDate() {
        Validation validation = new Validation() {
        };
        Spot spot = (Spot) fillObjects(new Spot());
        Calendar cal = Calendar.getInstance();
        spot.setValueDate(cal.getTime());
        spot.setTradeDate(cal.getTime());

        spot.getCustomerEntity().setId(1l);
        spot.getCurrencyPair().getCurrency1().setName("cc1");
        spot.getCurrencyPair().getCurrency2().setName("cc2");

        ruleEngine.fireRules(spot, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 0 messages", messages.size() == 0);
    }

    @Test
    public void testTrade() {
        Validation validation = new Validation() {
        };
        ruleEngine.fireRules(fillObjects(new Spot()), validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 5 messages", messages.size() == 5);
        Assert.assertTrue("EmptyTradeDate", messages.get(0).getCode().equals("EmptyTradeDate"));
        Assert.assertTrue("EmptyValueDate", messages.get(1).getCode().equals("EmptyValueDate"));
        Assert.assertTrue("InvalidCustomer", messages.get(2).getCode().equals("InvalidCustomer"));
        Assert.assertTrue("InvalidCurrency", messages.get(3).getCode().equals("InvalidCurrency"));
        Assert.assertTrue("InvalidCurrency", messages.get(4).getCode().equals("InvalidCurrency"));
    }

    @Test
    public void testInvalidSpotDate() {
        when(fixerIOApiGatewayService.businessDaysBetween(any(), any())).thenReturn(3);
        Validation validation = new Validation() {
        };
        Spot spot = (Spot) fillObjects(new Spot());
        spot.setTradeDate(new Date());
        spot.setValueDate(new Date());
        spot.getCustomerEntity().setId(1l);
        spot.getCurrencyPair().getCurrency1().setName("cc1");
        spot.getCurrencyPair().getCurrency2().setName("cc2");

        ruleEngine.fireRules(spot, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 1 messages", messages.size() == 1);
        Assert.assertTrue("InvalidSpotDate", messages.get(0).getCode().equals("InvalidSpotDate"));

    }

    @Test
    public void testValidSpotDate() {
        Validation validation = new Validation() {
        };
        Spot spot = (Spot) fillObjects(new Spot());
        spot.setTradeDate(new Date());
        spot.setValueDate(new Date());
        spot.getCustomerEntity().setId(1l);
        spot.getCurrencyPair().getCurrency1().setName("cc1");
        spot.getCurrencyPair().getCurrency2().setName("cc2");

        ruleEngine.fireRules(spot, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 0 messages", messages.size() == 0);
    }

    @Test
    public void testInvalidForwardDate() {
        Validation validation = new Validation() {
        };
        Forward forward = (Forward) fillObjects(new Forward());
        forward.setTradeDate(new Date());
        forward.setValueDate(new Date());
        forward.getCustomerEntity().setId(1l);
        forward.getCurrencyPair().getCurrency1().setName("cc1");
        forward.getCurrencyPair().getCurrency2().setName("cc2");

        ruleEngine.fireRules(forward, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 1 messages", messages.size() == 1);
        Assert.assertTrue("InvalidForwardDate", messages.get(0).getCode().equals("InvalidForwardDate"));

    }

    @Test
    public void testValidForwardDate() {
        when(fixerIOApiGatewayService.businessDaysBetween(any(), any())).thenReturn(3);
        Validation validation = new Validation() {
        };
        Forward forward = (Forward) fillObjects(new Forward());
        forward.setTradeDate(new Date());
        forward.setValueDate(new Date());
        forward.getCustomerEntity().setId(1l);
        forward.getCurrencyPair().getCurrency1().setName("cc1");
        forward.getCurrencyPair().getCurrency2().setName("cc2");

        ruleEngine.fireRules(forward, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 0 messages", messages.size() == 0);
    }

    @Test
    public void testInvalidOption() {
        Validation validation = new Validation() {
        };
        VanillaOption option = (VanillaOption) fillObjects(new EuropeanVanillaOption());
        option.setTradeDate(new Date());
        option.getCustomerEntity().setId(1l);
        option.getCurrencyPair().getCurrency1().setName("cc1");
        option.getCurrencyPair().getCurrency2().setName("cc2");

        ruleEngine.fireRules(option, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 3 messages", messages.size() == 3);
        Assert.assertTrue("EmptyExpiryDate", messages.get(0).getCode().equals("EmptyExpiryDate"));
        Assert.assertTrue("EmptyPremiumDate", messages.get(1).getCode().equals("EmptyPremiumDate"));
        Assert.assertTrue("EmptyDeliveryDate", messages.get(2).getCode().equals("EmptyDeliveryDate"));
    }

    @Test
    public void testInvalidOptionDates() {
        Validation validation = new Validation() {
        };
        VanillaOption option = (VanillaOption) fillObjects(new EuropeanVanillaOption());
        option.setTradeDate(new Date());
        option.getCustomerEntity().setId(1l);
        option.getCurrencyPair().getCurrency1().setName("cc1");
        option.getCurrencyPair().getCurrency2().setName("cc2");
        Calendar cal = Calendar.getInstance();
        option.setDeliveryDate(cal.getTime());
        cal.add(Calendar.DATE, 10);
        option.setExpiryDate(cal.getTime());
        option.setPremiumDate(cal.getTime());

        ruleEngine.fireRules(option, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 1 messages", messages.size() == 1);
        Assert.assertTrue("ExpiryOrPremiumDateAfterDelivery",
                messages.get(0).getCode().equals("ExpiryOrPremiumDateAfterDelivery"));
    }

    @Test
    public void testValidOptions() {
        Validation validation = new Validation() {
        };
        VanillaOption option = (VanillaOption) fillObjects(new EuropeanVanillaOption());
        option.setTradeDate(new Date());
        option.getCustomerEntity().setId(1l);
        option.getCurrencyPair().getCurrency1().setName("cc1");
        option.getCurrencyPair().getCurrency2().setName("cc2");

        Calendar cal = Calendar.getInstance();
        option.setDeliveryDate(cal.getTime());
        option.setExpiryDate(cal.getTime());
        option.setPremiumDate(cal.getTime());

        ruleEngine.fireRules(option, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 0 messages", messages.size() == 0);
    }

    @Test
    public void testInvalidAmericanOption() {
        Validation validation = new Validation() {
        };
        VanillaOption option = (VanillaOption) fillObjects(new AmericanVanillaOption());
        option.setTradeDate(new Date());
        option.getCustomerEntity().setId(1l);
        option.getCurrencyPair().getCurrency1().setName("cc1");
        option.getCurrencyPair().getCurrency2().setName("cc2");

        Calendar cal = Calendar.getInstance();
        option.setDeliveryDate(cal.getTime());
        option.setExpiryDate(cal.getTime());
        option.setPremiumDate(cal.getTime());

        ruleEngine.fireRules(option, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 1 messages", messages.size() == 1);
        Assert.assertTrue("EmptyExerciseStartDate",
                messages.get(0).getCode().equals("EmptyExerciseStartDate"));

    }

    @Test
    public void testInvalidAmericanOptionExerciseStartDate() {
        Validation validation = new Validation() {
        };
        AmericanVanillaOption option = (AmericanVanillaOption) fillObjects(new AmericanVanillaOption());
        option.setTradeDate(new Date());
        option.getCustomerEntity().setId(1l);
        option.getCurrencyPair().getCurrency1().setName("cc1");
        option.getCurrencyPair().getCurrency2().setName("cc2");

        Calendar cal = Calendar.getInstance();
        option.setDeliveryDate(cal.getTime());
        option.setExpiryDate(cal.getTime());
        option.setPremiumDate(cal.getTime());
        cal.add(Calendar.DATE, 10);
        option.setExcerciseStartDate(cal.getTime());

        ruleEngine.fireRules(option, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 1 messages", messages.size() == 1);
        Assert.assertTrue("ExerciseStartDateAfterExpiryDate",
                messages.get(0).getCode().equals("ExerciseStartDateAfterExpiryDate"));

    }

    @Test
    public void testValidAmericanOptionExerciseStartDate() {
        Validation validation = new Validation() {
        };
        AmericanVanillaOption option = (AmericanVanillaOption) fillObjects(new AmericanVanillaOption());
        option.setTradeDate(new Date());
        option.getCustomerEntity().setId(1l);
        option.getCurrencyPair().getCurrency1().setName("cc1");
        option.getCurrencyPair().getCurrency2().setName("cc2");

        Calendar cal = Calendar.getInstance();
        option.setDeliveryDate(cal.getTime());
        option.setExpiryDate(cal.getTime());
        option.setPremiumDate(cal.getTime());
        option.setExcerciseStartDate(cal.getTime());

        ruleEngine.fireRules(option, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 0 messages", messages.size() == 0);

    }


    private FXTransaction fillObjects(FXTransaction transaction) {
        transaction.setCustomerEntity(new Customer());
        CurrencyPair currencyPair = new CurrencyPair();
        currencyPair.setCurrency1(new Currency());
        currencyPair.setCurrency2(new Currency());
        transaction.setCurrencyPair(currencyPair);
        return transaction;
    }
}