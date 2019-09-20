package com.metallica.tradeservice.controller;

import com.metallica.tradeservice.common.MetallicaTradeConstants;
import com.metallica.tradeservice.exception.PriceNotFoundException;
import com.metallica.tradeservice.model.Price;
import com.metallica.tradeservice.model.Trade;
import com.metallica.tradeservice.rabbitmq.TradeStatus;
import com.metallica.tradeservice.service.ITrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/trade")
public
class TradeController {

    private static
    Logger loggerFactory = LoggerFactory.getLogger ( TradeController.class );

    @Value("${rabbitmq.exchange-name}")
    private String exchange;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Autowired
    private
    ITrade tradeService;

    @Autowired
    private
    RestTemplate restTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private Environment env;

    private Trade trade;

    @RabbitHandler
    @PostMapping("/buy")
    public
    ResponseEntity<Object> buyTrade(@RequestBody Trade trade){
        this.trade = trade;
        Price commodity = restTemplate.getForObject ( "http://"+env.getProperty ( "service.zuul-api-gateway-server.name" )+"/"+
                env.getProperty ("service.market-data-service.name")+"/"+
                env.getProperty ( "service.market-data-service.root-path" )+"/"+
                env.getProperty ( "service.market-data-service.get-price" )+"/"+
                trade.getCommodity (), Price.class );

        if(commodity == null){
            throw new PriceNotFoundException ("Exception occured in {} "+this.getClass () +". " +
                    "No commodity found {} "+trade.getCommodity ());
        }

        loggerFactory.info ( "Commodity details : "+commodity );
        trade.setTradeStatus ( MetallicaTradeConstants.TRADE_STATUS_INITIATED );
        trade.setPrice ( commodity.getPrice () );
        Trade savedTrade = null;
        try {
            savedTrade = saveTrade();
        }
        catch(Exception ex){
            loggerFactory.info ( "Error in saving trade to DB {} -> "+ex );
        }
        loggerFactory.info ( "Trade saved successfully to DB "+savedTrade );

        loggerFactory.info ( "Sending trade to Queue {} in "+ this.getClass ().getName () );
        sendDataToQueue(savedTrade);

        // getting URI of trade
        loggerFactory.info ( "getting URI if created trade in {} "+this.getClass ().getMethods () );
        URI location = getUriOfTrade(savedTrade);

        return ResponseEntity.created ( location ).build ();
    }


    @PutMapping("/update")
    public void updateTradeStatus(@RequestBody TradeStatus _tradeStatus){
        loggerFactory.info ( "Updating {} "+_tradeStatus+" to DB in Trade Service" );
        Long id = null;
        String tradeStatus = null;
        try {
            id = _tradeStatus.getId ( );
            tradeStatus = _tradeStatus.getTradeStatus ( );
            tradeService.updateTradeStatus (id , tradeStatus );
        }
        catch(Exception ex){
            loggerFactory.info ( "Error while updating trade status {} "+tradeStatus +". Pushing this trade to pending status." );
        }
    }

    private Trade saveTrade(){
       return tradeService.save ( trade );
    }

    private void sendDataToQueue( Trade savedTrade){
        if(null != savedTrade){
            TradeStatus tradeStatus = new TradeStatus (  );
            tradeStatus.setId (savedTrade.getId ());
            tradeStatus.setTradeStatus ( savedTrade.getTradeStatus () );
            try {
                amqpTemplate.convertAndSend ( exchange , routingKey , tradeStatus );
            }catch(Exception exception){
                loggerFactory.info ( "Exception occurred while sending Data to Queue {} -> "+exception );
            }
        }
    }

    private
    URI getUriOfTrade (Trade savedTrade) {
        return ServletUriComponentsBuilder.fromCurrentRequest ()
                .path ( "/{id}" )
                .buildAndExpand ( savedTrade.getId ())
                .toUri ();
    }
}
