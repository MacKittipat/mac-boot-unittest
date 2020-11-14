package com.mackittipat.boot.unittest.model;

import lombok.Data;

@Data
public class CurrencyRateRequest {

    private String baseCurrency;
    private String targetCurrency;
    private double amount;

}
