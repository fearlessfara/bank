package com.bok.bank.util;

import com.bok.bank.model.ExchangeCurrencyValueHistory;
import com.bok.bank.repository.ExchangeCurrencyValueHistoryRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * API data : https://app.exchangerate-api.com/dashboard
 */
@Slf4j
@Component
public class ExchangeData {

    @Autowired
    ExchangeCurrencyValueHistoryRepository exchangeCurrencyValueHistoryRepository;

    @Value("${exchange-currency.api-key}")
    private String apiKey;

    @Value("${exchange-currency.endpoint}")
    private String endpoint;

    private final String LATEST = "/latest/";

    private final List<String> CURRENCIES = Arrays.asList("USD", "EUR", "GBP");

    /**
     * API GET https://v6.exchangerate-api.com/v6/ceb94d443f6398dd5e640cd1/latest/USD
     */
    public ExchangeCurrencyDTO fetchData() {

        try {
            return makeAPICall(endpoint + apiKey + LATEST + "USD");
        } catch (IOException e) {
            log.error("Error: cannont access content - " + e.toString());
        }
        return null;
    }

    @Scheduled(fixedDelay = 50000000, initialDelay = 1000)
    public void updateDatabaseCurrenciesExchange() {
        PageRequest pageRequest = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        Page<ExchangeCurrencyValueHistory> exchangeCurrencyValuesPage = exchangeCurrencyValueHistoryRepository.findAll(pageRequest);
        if (!exchangeCurrencyValuesPage.isEmpty() && exchangeCurrencyValuesPage.getContent().get(0).getTime_next_update_unix().isAfter(Instant.now())) {
            return;
        }
        List<ExchangeCurrencyValueHistory> exchangeCurrencyValueHistoryToSave = new ArrayList<>();
        for (String curr : CURRENCIES) {
            try {
                ExchangeCurrencyDTO exchangeCurrencyDTO = makeAPICall(endpoint + apiKey + LATEST + curr);
                exchangeCurrencyValueHistoryToSave.add(exchangeCurrencyDTO.toExchangeCurrencyValues(curr));
            } catch (IOException e) {
                log.error("Error: cannont access content - " + e.toString());
            }
        }
        exchangeCurrencyValueHistoryRepository.saveAll(exchangeCurrencyValueHistoryToSave);
    }

    private ExchangeCurrencyDTO makeAPICall(String uri) throws IOException {
        // Making Request
        URL url = new URL(uri);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();


        Gson gson = new Gson();
        return gson.fromJson(jsonobj, ExchangeCurrencyDTO.class);
    }
}
