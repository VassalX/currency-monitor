package com.currency.currencymonitor.repository;

import com.currency.currencymonitor.model.Currency;
import com.currency.currencymonitor.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long> {
    List<History> findByCurrencyOrderByTimestampDesc(Currency currency);

    @Query(value = "select * from history where currency_id = ?1 order by timestamp desc limit 1 offset 1", nativeQuery = true)
    List<History> findByCurrencyLastPrice(long id);
}
