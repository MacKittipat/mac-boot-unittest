package com.mackittipat.boot.unittest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mackittipat.boot.unittest.exception.CurrencyConverterException;
import com.mackittipat.boot.unittest.model.CurrencyRateRequest;
import com.mackittipat.boot.unittest.model.CurrencyRateResponse;
import com.mackittipat.boot.unittest.service.EcbCurrencyConverterService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CurrencyRateController.class)
class CurrencyRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EcbCurrencyConverterService ecbCurrencyConverterService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testConvertRate() throws Exception {

        CurrencyRateRequest currencyRateRequest = new CurrencyRateRequest();
        currencyRateRequest.setAmount(100.00);
        currencyRateRequest.setBaseCurrency("THB");
        currencyRateRequest.setTargetCurrency("USD");

        CurrencyRateResponse currencyRateResponse = new CurrencyRateResponse();
        currencyRateResponse.setAmount(3.00);

        Mockito.when(ecbCurrencyConverterService.convertRate(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()))
                .thenReturn(3.00);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/currencies/rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyRateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(currencyRateResponse)));
        Mockito.verify(ecbCurrencyConverterService, Mockito.times(1))
                .convertRate(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble());
    }

    @Test
    public void testConvertRateThrowException() throws Exception {

        CurrencyRateRequest currencyRateRequest = new CurrencyRateRequest();
        currencyRateRequest.setAmount(100.00);
        currencyRateRequest.setBaseCurrency("THB");
        currencyRateRequest.setTargetCurrency("USD");

        Mockito.when(ecbCurrencyConverterService.convertRate(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()))
                .thenThrow(CurrencyConverterException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/currencies/rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyRateRequest)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
