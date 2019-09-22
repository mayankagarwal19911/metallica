package com.metallica.marketdataservice.controller;

import com.metallica.marketdataservice.model.Price;
import com.metallica.marketdataservice.service.IMarketDatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mds")
public
class MarketDataService {

    private static
    Logger loggerFactory = LoggerFactory.getLogger ( MarketDataService.class );

    @Autowired
    private
    IMarketDatService marketDatService;

    @PostMapping("/put/")
    public void addPrices(@RequestBody Price price){
        loggerFactory.info ( "Price is : "+price );
        marketDatService.save ( price );
    }

    @GetMapping("/get-price/{commodity}")
    public
    Price getPrices(@PathVariable String commodity){
        loggerFactory.info ( "Getting price for "+commodity );
        try {
            return marketDatService.findByCommodity ( commodity );
        }catch (Exception ex){
            loggerFactory.info ( "Exception occurred in "+this.getClass ().getName () +" " +
                    "while getting price for {} "+commodity +" : "+ex);
        }
        return null;
    }

}
