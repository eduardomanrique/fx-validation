package com.eduardomanrique.fxvalidation.products.deserializer;

import com.eduardomanrique.fxvalidation.DateUtil;
import com.eduardomanrique.fxvalidation.entity.Currency;
import com.eduardomanrique.fxvalidation.entity.Customer;
import com.eduardomanrique.fxvalidation.products.*;
import com.eduardomanrique.fxvalidation.service.CurrencyService;
import com.eduardomanrique.fxvalidation.service.CustomerService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class FXTransactionDeserializer extends JsonDeserializer<FXTransaction> {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CustomerService customerService;

    @Override
    public FXTransaction deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Optional<? extends FXTransaction> result = Optional.empty();
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.has("type")) {
            String type = text(node, "type");
            if (type.equalsIgnoreCase("spot")) {
                result = createSpot(node);
            } else if (type.equalsIgnoreCase("forward")) {
                result = createForward(node);
            } else if (type.equalsIgnoreCase("vanillaOption")) {
                result = createVanillaOption(node);
            }
            result.ifPresent(transaction -> fillCommonFields(node, transaction));
        }
        return result.orElse(null);
    }

    @SneakyThrows
    private Optional<VanillaOption> createVanillaOption(JsonNode node) {
        Optional<VanillaOption> result = Optional.empty();
        if (node.has("style")) {
            VanillaOption vanilla = null;
            String style = text(node, "style");
            if (style.equalsIgnoreCase("american")) {
                vanilla = new AmericanVanillaOption();
                ((AmericanVanillaOption) vanilla).setExcerciseStartDate(
                        DateUtil.parse(Optional.ofNullable(text(node, "excerciseStartDate"))).orElse(null));
            } else if (style.equalsIgnoreCase("european")) {
                vanilla = new EuropeanVanillaOption();
            }
            if (vanilla != null) {
                vanilla.setStrategy(text(node, "strategy"));
                vanilla.setPremiumType(text(node, "premiumType"));
                vanilla.setDeliveryDate(DateUtil.parse(Optional.ofNullable(text(node, "deliveryDate"))).orElse(null));
                vanilla.setExpiryDate(DateUtil.parse(Optional.ofNullable(text(node, "expiryDate"))).orElse(null));
                vanilla.setPremiumDate(DateUtil.parse(Optional.ofNullable(text(node, "premiumDate"))).orElse(null));
                vanilla.setPremium(decimal(node, "premium"));
                String payCcyName = text(node, "payCcy");
                vanilla.setPayCcy(currencyService.getCurrencyByIsoCode(payCcyName).orElseGet(() -> new Currency(payCcyName)));
                String premiumCcyName = text(node, "premiumCcy");
                vanilla.setPremiumCcy(currencyService.getCurrencyByIsoCode(premiumCcyName).orElseGet(() -> new Currency(premiumCcyName)));
                result = Optional.of(vanilla);
            }
        }
        return result;
    }

    @SneakyThrows
    private Optional<Forward> createForward(JsonNode node) {
        Forward result = new Forward();
        result.setValueDate(DateUtil.parse(Optional.ofNullable(text(node, "valueDate"))).orElse(null));
        return Optional.of(result);
    }

    @SneakyThrows
    private Optional<Spot> createSpot(JsonNode node) {
        Spot result = new Spot();
        result.setValueDate(DateUtil.parse(Optional.ofNullable(text(node, "valueDate"))).orElse(null));
        return Optional.of(result);
    }

    @SneakyThrows
    private void fillCommonFields(JsonNode node, FXTransaction transaction) {
        String customerName = text(node, "customer");
        Optional<Customer> customer = customerService.getCustomerByName(customerName);
        transaction.setCustomer(customer.orElseGet(() -> new Customer(customerName)));

        CurrencyPair currencyPair = new CurrencyPair();
        String currencyPairStr = text(node, "ccyPair");
        String currencyName1 = currencyPairStr.substring(0, 3);
        currencyPair.setCurrency1(
                currencyService.getCurrencyByIsoCode(currencyName1)
                        .orElseGet(() -> new Currency(currencyName1))
        );
        String currencyName2 = currencyPairStr.substring(3);
        currencyPair.setCurrency2(
                currencyService.getCurrencyByIsoCode(currencyName2)
                        .orElseGet(() -> new Currency(currencyName2))
        );
        transaction.setCurrencyPair(currencyPair);
        transaction.setDirection(FXTransaction.Direction.valueOf(text(node, "direction").toUpperCase()));
        transaction.setTradeDate(DateUtil.parse(Optional.ofNullable(text(node, "tradeDate"))).orElse(null));
        transaction.setAmount1(decimal(node, "amount1"));
        transaction.setAmount2(decimal(node, "amount2"));
        transaction.setRate(decimal(node, "rate"));
        transaction.setLegalEntity(text(node, "legalEntity"));
        transaction.setTrader(text(node, "trader"));
    }

    private String text(JsonNode node, String name) {
        return node.has(name) ? node.get(name).asText() : null;
    }

    private BigDecimal decimal(JsonNode node, String name) {
        return node.has(name) ? node.get(name).decimalValue() : null;
    }
}


