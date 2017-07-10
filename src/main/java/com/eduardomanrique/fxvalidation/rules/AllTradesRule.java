package com.eduardomanrique.fxvalidation.rules;

import com.eduardomanrique.fxvalidation.products.FXTransaction;
import com.eduardomanrique.fxvalidation.products.Forward;
import com.eduardomanrique.fxvalidation.products.Spot;
import com.eduardomanrique.fxvalidation.rulesengine.Rule;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import com.eduardomanrique.fxvalidation.service.FixerIOApiGatewayService;
import com.eduardomanrique.fxvalidation.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class AllTradesRule implements Rule<FXTransaction> {

    @Autowired
    private FixerIOApiGatewayService fixerIOApiGatewayService;

    @Override
    public int getPriotiy() {
        return 0;
    }

    @Override
    public void onFact(FXTransaction fact, Validation validation) {
        log.debug("Rule for all trades: {}", fact);
        if (fact.getTradeDate() == null) {
            validation.addValidationError(Errors.EmptyTradeDate.name(), Errors.EmptyTradeDate.msg());
        }
        if (fact instanceof Spot || fact instanceof Forward) {
            checkValueDate(fact, validation);
        }
        if (fact.getCustomerEntity().getId() == null) {
            validation.addValidationError(Errors.InvalidCustomer.name(), Errors.InvalidCustomer.msg(fact.getCustomer()));
        }
        if (!fact.getCurrencyPair().getCurrency1().isPresent()) {
            validation.addValidationError(Errors.InvalidCurrency.name(), Errors.InvalidCurrency.msg());
        }
        if (!fact.getCurrencyPair().getCurrency2().isPresent()) {
            validation.addValidationError(Errors.InvalidCurrency.name(), Errors.InvalidCurrency.msg());
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
            validation.addValidationError(Errors.EmptyValueDate.name(),
                    Errors.EmptyValueDate.msg());
        } else {
            if (valueDate.before(fact.getTradeDate())) {
                validation.addValidationError(
                        Errors.ValueDateBeforeTradeDate.name(),
                        Errors.ValueDateBeforeTradeDate.msg(
                                DateUtil.format(valueDate), DateUtil.format(fact.getTradeDate())));

            }
            if (!fixerIOApiGatewayService.isBusinessDay(valueDate)) {
                validation.addValidationError(
                        Errors.ValueDateNotBusinessDate.name(),
                        Errors.ValueDateNotBusinessDate.msg(
                                DateUtil.format(valueDate), DateUtil.format(valueDate)));
            }
        }
    }
}
