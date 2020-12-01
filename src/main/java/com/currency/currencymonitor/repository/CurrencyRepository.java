package com.currency.currencymonitor.repository;

import com.currency.currencymonitor.model.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findAll();
    Optional<Currency> findByShortName(String shortName);
    Page<Currency> findAllByTypeAndShortNameContainingIgnoreCase(String type, String shortName, Pageable pageable);
    Page<Currency> findAllByType(String type, Pageable pageable);
}
