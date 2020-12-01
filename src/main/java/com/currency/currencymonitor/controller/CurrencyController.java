package com.currency.currencymonitor.controller;

import com.currency.currencymonitor.model.Currency;
import com.currency.currencymonitor.model.History;
import com.currency.currencymonitor.repository.CurrencyRepository;
import com.currency.currencymonitor.repository.HistoryRepository;
import com.currency.currencymonitor.response.CurrencyPriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value="/api")
public class CurrencyController {

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    HistoryRepository historyRepository;

    @PostMapping(value = "/currency",consumes = {"application/json"})
    public ResponseEntity<List<Currency>> updateCurrencies(@RequestBody List<Currency> currencies){
        try {
            List<Currency> newCurrencies = new ArrayList<>();
            for (Currency currency : currencies){
                String name = currency.getName();
                Optional<Currency> foundCurrency = currencyRepository.findByName(name);
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
    @GetMapping(value = "/currency")
    public ResponseEntity<Currency> getCurrencyInfo(@RequestParam Long id){
        Optional<Currency> currency = currencyRepository.findById(id);
        if (currency.isPresent()){
            return new ResponseEntity<>(currency.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //GET /currencies?type="crypto/fiat"&limit=10&offset=30&name=""&orderBy="fullName/price"&direction="asc/desc"
    @GetMapping(value = "/currencies")
    public ResponseEntity<List<Currency>> getCurrencies(@RequestParam(name = "type") String type,
                                                      @RequestParam(name = "limit") int limit,
                                                      @RequestParam(name = "offset") int offset,
                                                      @RequestParam(name = "name", required = false) String name,
                                                      @RequestParam(name = "orderBy") String orderBy,
                                                      @RequestParam(name = "direction") String direction){
        try{
            Sort sorting = (direction.toUpperCase().equals("ASC")) ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
            Pageable paging = PageRequest.of(offset / limit, limit, sorting);
            Page<Currency> pagedResult;
            if(name != null){
                pagedResult = currencyRepository.findAllByTypeAndNameContainingIgnoreCase(type, name, paging);
            } else {
                pagedResult = currencyRepository.findAllByType(type, paging);
            }
            return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET /currency-updates?id= - currency price update, {previousPrice: 1000, currentPrice: 1002, currentPriceUpdateTimestamp: some_timestamp}
    @GetMapping(value = "/currency-updates")
    public ResponseEntity<CurrencyPriceResponse> getCurrencyUpdates(@RequestParam long id){
        Optional<Currency> currency = currencyRepository.findById(id);

        if (currency.isPresent()){
            Currency _currency = currency.get();
            History prevHistory = historyRepository.findByCurrencyLastPrice(_currency.getId()).get(0);
            CurrencyPriceResponse response = new CurrencyPriceResponse(
                    prevHistory.getPrice(),
                    _currency.getPrice(),
                    _currency.getTimestamp()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET /currency-historical?id= - all currency prices, [{date: some_timestamp, price: 1002}]
    @GetMapping(value = "/currency-historical")
    public ResponseEntity<List<History>> getHistoryOf(@RequestParam long id){
        Optional<Currency> currency = currencyRepository.findById(id);

        if (currency.isPresent()){
            Currency _currency = currency.get();
            List<History> response = historyRepository.findByCurrencyOrderByTimestampDesc(_currency);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
