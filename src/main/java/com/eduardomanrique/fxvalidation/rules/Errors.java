package com.eduardomanrique.fxvalidation.rules;

public enum Errors {
    EmptyTradeDate("Trade date is null"),
    EmptyValueDate("Value date is null"),
    ValueDateBeforeTradeDate("Value date %s before trade date %s"),
    InvalidCustomer("Customer %s is not valid"),
    InvalidCurrency("Currency %s is not valid"),
    ValueDateNotBusinessDate("Value date must be a business day"),

    ExpiryOrPremiumDateAfterDelivery("Expiry date (%s) and premium date (%s) shall be before delivery date (%s)"),
    EmptyExpiryDate("Expiry date is null"),
    EmptyPremiumDate("Premium date is null"),
    EmptyDeliveryDate("Delivery date is null"),

    EmptyExerciseStartDate("Exercise start date is null"),
    ExerciseStartDateAfterExpiryDate("Exercise start date (%s) has to be before expiry date (%s)"),
    ExerciseStartDateBeforeTradeDate("Exercise start date (%s) has to be after trade date (%s)"),

    InvalidSpotDate("Spot date (%s) must be 2 days from trade date (%s)"),

    InvalidForwardDate("Forward date (%s) must be more than 2 days from trade date (%s)");

    private final String msg;

    Errors(String msg) {
        this.msg = msg;
    }

    public String msg(Object... parameters) {
        return String.format(this.msg, parameters);
    }
}
