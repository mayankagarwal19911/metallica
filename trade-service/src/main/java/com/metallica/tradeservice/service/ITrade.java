package com.metallica.tradeservice.service;

import com.metallica.tradeservice.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public
interface ITrade  extends JpaRepository<Trade, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("update Trade t set t.tradeStatus=:tradeStatus where t.id=:id")
    void updateTradeStatus (@Param("id") Long id,
            @Param("tradeStatus") String tradeStatus);
}
