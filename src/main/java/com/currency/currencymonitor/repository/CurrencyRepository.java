package com.currency.currencymonitor.repository;

import com.currency.currencymonitor.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findAll();
}
