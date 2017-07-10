package com.eduardomanrique.fxvalidation.rules;

import com.eduardomanrique.fxvalidation.products.AmericanVanillaOption;
import com.eduardomanrique.fxvalidation.rulesengine.Rule;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import com.eduardomanrique.fxvalidation.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AmericanOptionsRule implements Rule<AmericanVanillaOption> {

    @Override
    public int getPriotiy() {
        return 2;
    }

    @Override
    public void onFact(AmericanVanillaOption fact, Validation validation) {
        log.debug("Rule for all american options: {}", fact);
        if (fact.getExcerciseStartDate() == null) {
            validation.addValidationError(Errors.EmptyExerciseStartDate.name(),
                    Errors.EmptyExerciseStartDate.msg());
        } else {
            if (fact.getExpiryDate() != null && fact.getExcerciseStartDate().after(fact.getExpiryDate())) {
                validation.addValidationError(Errors.ExerciseStartDateAfterExpiryDate.name(),
                        Errors.ExerciseStartDateAfterExpiryDate.msg(
                                DateUtil.format(Optional.ofNullable(fact.getExcerciseStartDate())).get(),
                                DateUtil.format(Optional.ofNullable(fact.getExpiryDate())).get()
                        ));
            }
            if (fact.getTradeDate() != null && fact.getExcerciseStartDate().before(fact.getTradeDate())) {
                validation.addValidationError(Errors.ExerciseStartDateBeforeTradeDate.name(),
                        Errors.ExerciseStartDateAfterExpiryDate.msg(
                                DateUtil.format(Optional.ofNullable(fact.getExcerciseStartDate())).get(),
                                DateUtil.format(Optional.ofNullable(fact.getTradeDate())).get()
                        ));
            }
        }

    }

}
