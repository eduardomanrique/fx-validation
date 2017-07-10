package com.eduardomanrique.fxvalidation.rules;

import com.eduardomanrique.fxvalidation.products.Spot;
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
public class SpotRule implements Rule<Spot> {

    @Autowired
    private FixerIOApiGatewayService fixerIOApiGatewayService;

    @Override
    public int getPriotiy() {
        return 3;
    }

    @Override
    public void onFact(Spot fact, Validation validation) {
        log.debug("Rule for all spot: {}", fact);
        if (fact.getTradeDate() != null && fact.getValueDate() != null) {
            long diff = fixerIOApiGatewayService.businessDaysBetween(fact.getTradeDate(), fact.getValueDate());
            if (diff != 2) {
                validation.addValidationError(Errors.InvalidSpotDate.name(),
                        Errors.InvalidSpotDate.msg(
                                DateUtil.format(Optional.ofNullable(fact.getValueDate())).get(),
                                DateUtil.format(Optional.ofNullable(fact.getTradeDate())).get()
                        ));
            }
        }
    }

}
