package com.mackittipat.boot.unittest.service;

import com.mackittipat.boot.unittest.exception.CurrencyConverterException;
import com.mackittipat.boot.unittest.model.api.ExchangeRateApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class EcbCurrencyConverterService implements CurrencyConverterService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.currency.europeancentralbank.endpoint}")
    private String apiEndpoint;

    @Override
    public double convertRate(String baseCurrency, String targetCurrency, double amount)
            throws CurrencyConverterException {

        ExchangeRateApiResponse exchangeRateApiResponse =
                restTemplate.getForObject(
                        apiEndpoint + "/latest?&base=" + baseCurrency,
                        ExchangeRateApiResponse.class);

        if (exchangeRateApiResponse == null ||
                exchangeRateApiResponse.getRates().size() == 0 ||
                exchangeRateApiResponse.getRates().get(targetCurrency) == null) {
            throw new CurrencyConverterException("Cannot convert rate from " + baseCurrency + " to " + targetCurrency);
        }

        return amount * exchangeRateApiResponse.getRates().get(targetCurrency);
    }
}
