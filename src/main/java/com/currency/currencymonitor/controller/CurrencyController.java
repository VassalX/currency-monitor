package com.currency.currencymonitor.controller;

import com.currency.currencymonitor.model.Currency;
import com.currency.currencymonitor.model.History;
import com.currency.currencymonitor.repository.CurrencyRepository;
import com.currency.currencymonitor.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value="/currency", consumes="application/json")
public class CurrencyController {

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    HistoryRepository historyRepository;

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<List<Currency>> updateCurrencies(@RequestBody List<Currency> currencies){
        try {
            List<Currency> newCurrencies = new ArrayList<>();
            for (Currency currency : currencies){
                String shortName = currency.getShortName();
                Optional<Currency> foundCurrency = currencyRepository.findByShortName(shortName);
                Currency _currency;
                if(foundCurrency.isPresent()){
                    _currency = foundCurrency.get();
                    if(_currency.getTimestamp() < currency.getTimestamp()){
                        _currency.addToHistories(new History(
                                currency.getPrice(),
                                currency.getTimestamp()
                        ));
                        _currency.setPrice(currency.getPrice());
                        _currency.setGrowth(currency.getGrowth());
                        _currency.setTimestamp(currency.getTimestamp());
                    }
                } else{
                    _currency = currency;
                    _currency.addToHistories(new History(
                            currency.getPrice(),
                            currency.getTimestamp()));
                }
                newCurrencies.add(_currency);
            }
            //System.out.println(newCurrencies);
            currencyRepository.saveAll(newCurrencies);
            return new ResponseEntity<>(newCurrencies, HttpStatus.ACCEPTED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET /currency?id=... - full info about currency
    @GetMapping
    public ResponseEntity<Currency> getCurrencyInfo(@RequestParam long id){
        Optional<Currency> currency = currencyRepository.findById(id);

        if (currency.isPresent()){
            return new ResponseEntity<>(currency.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //GET /currencies?type="crypto/fiat"&limit=10&offset=30&name=""&orderBy="fullName/price"&direction="asc/desc"
    @GetMapping
    public ResponseEntity<Currency> getCurrenciesList(@RequestParam String type,
                                                      @RequestParam int limit,
                                                      @RequestParam int offset,
                                                      @RequestParam String name,
                                                      @RequestParam String orderedBy,
                                                      @RequestParam String direction){

    }
}
