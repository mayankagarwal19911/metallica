package com.metallica.marketdataservice.service;

import com.metallica.marketdataservice.model.Price;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public
interface IMarketDatService extends CrudRepository<Price, Integer> {

    Price findByCommodity(String commodity);
}
