package com.eduardomanrique.fxvalidation.rules;

import com.eduardomanrique.fxvalidation.products.FXTransaction;
import com.eduardomanrique.fxvalidation.rulesengine.Rule;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestRule implements Rule<List> {
    @Override
    public int getPriotiy() {
        return 0;
    }

    @Override
    public void onFact(List fact, Validation validation) {
        System.out.println("======TEST RULE");
    }
}
