package com.eduardomanrique.fxvalidation.rulesengine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class RuleEngineTest {

    RuleEngine ruleEngine;

    @Before
    public void before() {
        ruleEngine = new RuleEngine(Arrays.asList(
                new Rule<Object>() {
                    public int getPriotiy() {
                        return 0;
                    }

                    public void onFact(Object fact, Validation validation) {
                        validation.addValidationError("ObjectRule", "...");
                    }
                },
                new Rule<Number>() {
                    public int getPriotiy() {
                        return 1;
                    }

                    public void onFact(Number fact, Validation validation) {
                        validation.addValidationError("NumberRule", "...");
                    }
                },
                new Rule<String>() {
                    public int getPriotiy() {
                        return 2;
                    }

                    public void onFact(String fact, Validation validation) {
                        validation.addValidationError("StringRule", "...");
                    }
                }));
    }

    @Test
    public void testObject() {
        Validation validation = new Validation() {
        };
        ruleEngine.fireRules(new Object(), validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 1 messages", messages.size() == 1);
        Assert.assertTrue("Should have ObjectRule message", messages.get(0).getCode().equals("ObjectRule"));
    }

    @Test
    public void testString() {
        Validation validation = new Validation() {
        };
        ruleEngine.fireRules("test", validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 2 messages", messages.size() == 2);
        Assert.assertTrue("Should have ObjectRule message", messages.get(0).getCode().equals("ObjectRule"));
        Assert.assertTrue("Should have StringRule message", messages.get(1).getCode().equals("StringRule"));
    }

    @Test
    public void testNumber() {
        Validation validation = new Validation() {
        };
        ruleEngine.fireRules(1, validation);
        List<Validation.ErrorMessage> messages = validation.getErrorMessages();
        Assert.assertTrue("Should have 2 messages", messages.size() == 2);
        Assert.assertTrue("Should have ObjectRule message", messages.get(0).getCode().equals("ObjectRule"));
        Assert.assertTrue("Should have NumberRule message", messages.get(1).getCode().equals("NumberRule"));
    }
}