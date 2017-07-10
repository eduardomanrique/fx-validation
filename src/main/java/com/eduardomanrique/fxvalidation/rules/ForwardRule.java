package com.eduardomanrique.fxvalidation.rules;

import com.eduardomanrique.fxvalidation.products.Forward;
import com.eduardomanrique.fxvalidation.rulesengine.Rule;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import com.eduardomanrique.fxvalidation.service.FixerIOApiGatewayService;
import com.eduardomanrique.fxvalidation.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ForwardRule implements Rule<Forward> {

    @Autowired
    private FixerIOApiGatewayService fixerIOApiGatewayService;

    @Override
    public int getPriotiy() {
        return 4;
    }

    @Override
    public void onFact(Forward fact, Validation validation) {
        log.debug("Rule for all forward: {}", fact);
        if (fact.getTradeDate() != null && fact.getValueDate() != null) {
            long diff = fixerIOApiGatewayService.businessDaysBetween(fact.getTradeDate(), fact.getValueDate());
            if (diff <= 2) {
                validation.addValidationError(Errors.InvalidForwardDate.name(),
                        Errors.InvalidForwardDate.msg(
                                DateUtil.format(Optional.ofNullable(fact.getValueDate())).get(),
                                DateUtil.format(Optional.ofNullable(fact.getTradeDate())).get()
                        ));
            }
        }
    }

}
