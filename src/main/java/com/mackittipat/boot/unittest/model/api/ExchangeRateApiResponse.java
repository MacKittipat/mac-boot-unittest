package com.mackittipat.boot.unittest.model.api;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRateApiResponse {

    private String base;
    private String date;
    private Map<String, Double> rates;

}
