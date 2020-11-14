package com.mackittipat.boot.unittest.service;

import com.mackittipat.boot.unittest.exception.CurrencyConverterException;
import com.mackittipat.boot.unittest.model.api.ExchangeRateApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
class EcbCurrencyConverterServiceTest {

    @InjectMocks
    private EcbCurrencyConverterService ecbCurrencyConverterService;

    @Mock
    private RestTemplate restTemplate;

    static Stream<Arguments> testConvertRateData() {
        return Stream.of(
                Arguments.arguments(1, 30, 30),
                Arguments.arguments(10, 30, 300),
                Arguments.arguments(30, 30, 900),
                Arguments.arguments(0, 30, 0),
                Arguments.arguments(30, 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("testConvertRateData")
    public void testConvertRate(double amount, double rate, double expected) throws Exception {

        Map<String, Double> rates = new HashMap<>();
        rates.put("THB", rate);

        ExchangeRateApiResponse exchangeRateApiResponse = new ExchangeRateApiResponse();
        exchangeRateApiResponse.setRates(rates);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(exchangeRateApiResponse);

        Assertions.assertEquals(expected, ecbCurrencyConverterService.convertRate("USD", "THB", amount));
    }

    @Test()
    public void testConvertRateApiReturnNull() throws Exception {

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(null);

        Exception exception = assertThrows(
                CurrencyConverterException.class,
                () -> ecbCurrencyConverterService.convertRate("USD", "THB", 100));
        Assertions.assertEquals("Cannot convert rate from USD to THB", exception.getMessage());
    }

    @Test
    public void testConvertRateApiReturnRateNull() throws Exception {

        ExchangeRateApiResponse exchangeRateApiResponse = new ExchangeRateApiResponse();
        exchangeRateApiResponse.setRates(null);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(exchangeRateApiResponse);

        Exception exception = assertThrows(
                CurrencyConverterException.class,
                () -> ecbCurrencyConverterService.convertRate("USD", "THB", 100));
        Assertions.assertEquals("Cannot convert rate from USD to THB", exception.getMessage());
    }
}
