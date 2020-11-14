package com.mackittipat.boot.unittest.api;

import com.mackittipat.boot.unittest.exception.CurrencyConverterException;
import com.mackittipat.boot.unittest.model.CurrencyRateRequest;
import com.mackittipat.boot.unittest.model.CurrencyRateResponse;
import com.mackittipat.boot.unittest.service.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyRateController {

    @Autowired
    private CurrencyConverterService currencyConverterService;

    @PostMapping("/currencies/rates")
    public CurrencyRateResponse convertRate(@RequestBody CurrencyRateRequest currencyRateRequest)
            throws CurrencyConverterException {
        CurrencyRateResponse currencyRateResponse = new CurrencyRateResponse();
        currencyRateResponse.setAmount(currencyConverterService.convertRate(
                currencyRateRequest.getBaseCurrency(),
                currencyRateRequest.getTargetCurrency(),
                currencyRateRequest.getAmount()));
        return currencyRateResponse;
    }
}
