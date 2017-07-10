package com.eduardomanrique.fxvalidation.rules;

import com.eduardomanrique.fxvalidation.products.VanillaOption;
import com.eduardomanrique.fxvalidation.rulesengine.Rule;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import com.eduardomanrique.fxvalidation.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AllOptionsRule implements Rule<VanillaOption> {

    @Override
    public int getPriotiy() {
        return 1;
    }

    @Override
    public void onFact(VanillaOption fact, Validation validation) {
        log.debug("Rule for all options: {}", fact);
        if (fact.getExpiryDate() == null) {
            validation.addValidationError(Errors.EmptyExpiryDate.name(),
                    Errors.EmptyExpiryDate.msg());
        }
        if (fact.getPremiumDate() == null) {
            validation.addValidationError(Errors.EmptyPremiumDate.name(),
                    Errors.EmptyPremiumDate.msg());
        }
        if (fact.getDeliveryDate() == null) {
            validation.addValidationError(Errors.EmptyDeliveryDate.name(),
                    Errors.EmptyDeliveryDate.msg());
        }
        if (fact.getExpiryDate() != null && fact.getPremiumDate() != null &&
                fact.getDeliveryDate() != null) {
            if (fact.getDeliveryDate().before(fact.getExpiryDate()) ||
                    fact.getDeliveryDate().before(fact.getPremiumDate())) {
                validation.addValidationError(Errors.ExpiryOrPremiumDateAfterDelivery.name(),
                        Errors.ExpiryOrPremiumDateAfterDelivery.msg(
                                DateUtil.format(Optional.ofNullable(fact.getExpiryDate())).get(),
                                DateUtil.format(Optional.ofNullable(fact.getPremiumDate())).get(),
                                DateUtil.format(Optional.ofNullable(fact.getDeliveryDate())).get()
                        ));
            }
        }
    }

}
