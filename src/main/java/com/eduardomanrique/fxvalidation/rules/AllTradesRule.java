package com.eduardomanrique.fxvalidation.rules;

import com.eduardomanrique.fxvalidation.DateUtil;
import com.eduardomanrique.fxvalidation.products.FXTransaction;
import com.eduardomanrique.fxvalidation.products.Forward;
import com.eduardomanrique.fxvalidation.products.Spot;
import com.eduardomanrique.fxvalidation.rulesengine.Rule;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class AllTradesRule implements Rule<FXTransaction> {

    public enum Errors {
        EmptyTradeDate("Trade date is null"),
        EmptyVaueDate("Value date is null"),
        ValueDateBeforeTradeDate("Value date %s before trade date %s"),
        InvalidCustomer("Customer %s is not valid"),
        InvalidCurrency("Currency %s is not valid");

        private final String msg;

        Errors(String msg) {
            this.msg = msg;
        }

        public String msg(Object... parameters) {
            return String.format(this.msg, parameters);
        }
    }

    @Override
    public int getPriotiy() {
        return 0;
    }

    @Override
    public void onFact(FXTransaction fact, Validation validation) {
        log.debug("Rule for all trades: {}", fact);
        if (fact.getTradeDate() == null) {
            validation.addValidationError(Errors.EmptyTradeDate.name(),
                    Errors.EmptyTradeDate.msg());
        }
        if (fact instanceof Spot || fact instanceof Forward) {
            checkValueDate(fact, validation);
        }
        if (fact.getCustomer().getId() == null) {
            validation.addValidationError(Errors.InvalidCustomer.name(),
                    Errors.InvalidCustomer.msg(fact.getCustomer().getName()));
        }
        if (fact.getCurrencyPair().getCurrency1().getName() == null) {
            validation.addValidationError(Errors.InvalidCurrency.name(),
                    Errors.InvalidCurrency.msg(fact.getCurrencyPair().getCurrency1().getIsoCode()));
        }
        if (fact.getCurrencyPair().getCurrency2().getName() == null) {
            validation.addValidationError(Errors.InvalidCurrency.name(),
                    Errors.InvalidCurrency.msg(fact.getCurrencyPair().getCurrency2().getIsoCode()));
        }
    }

    private void checkValueDate(FXTransaction fact, Validation validation) {
        Date valueDate;
        if (fact instanceof Spot) {
            valueDate = ((Spot) fact).getValueDate();
        } else {
            valueDate = ((Forward) fact).getValueDate();
        }
        if (valueDate == null) {
            validation.addValidationError(Errors.EmptyVaueDate.name(),
                    Errors.EmptyVaueDate.msg());
        }
        if (valueDate.before(fact.getTradeDate())) {
            validation.addValidationError(
                    Errors.ValueDateBeforeTradeDate.name(),
                    Errors.ValueDateBeforeTradeDate.msg(
                            DateUtil.format(valueDate), DateUtil.format(fact.getTradeDate())));
        }
        //TODO check fixio
    }
}
