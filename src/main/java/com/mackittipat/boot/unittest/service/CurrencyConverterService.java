package com.mackittipat.boot.unittest.service;

import com.mackittipat.boot.unittest.exception.CurrencyConverterException;

public interface CurrencyConverterService {

    double convertRate(String baseCurrency, String targetCurrency, double amount) throws CurrencyConverterException;

}
