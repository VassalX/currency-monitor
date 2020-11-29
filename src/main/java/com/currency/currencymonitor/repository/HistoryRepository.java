package com.currency.currencymonitor.repository;

import com.currency.currencymonitor.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long> {
    List<History> findByCurrencyId(Long currencyId);
}
