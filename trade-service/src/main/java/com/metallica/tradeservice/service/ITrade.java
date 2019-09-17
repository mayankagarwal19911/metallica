package com.metallica.tradeservice.service;

import com.metallica.tradeservice.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public
interface ITrade  extends CrudRepository<Trade, Integer> {
    Trade findById(Long id);
}
