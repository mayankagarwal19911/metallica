package com.metallica.tradeservice.controller;

import com.metallica.tradeservice.common.MetallicaTradeConstants;
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
import org.springframework.cloud.context.config.annotation.RefreshScope;
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

        loggerFactory.info ( " Commodity details : "+commodity );
        trade.setTradeStatus ( MetallicaTradeConstants.TRADE_STATUS );
        trade.setPrice ( commodity.getPrice () );
        Trade savedTrade = tradeService.save ( trade );

        loggerFactory.info ( "Working with RabbitMQ {} in "+ this.getClass ().getName () );
        sendDataToQueue(savedTrade);

        // getting URI of trade
        loggerFactory.info ( "getting URI if created trade in {} "+this.getClass ().getMethods () );
        URI location = getUriOfTrade(savedTrade);

        return ResponseEntity.created ( location ).build ();
    }


    @PutMapping("/update")
    public void updateTradeStatus(@RequestBody TradeStatus tradeStatus){
        loggerFactory.info ( "Updating {} "+tradeStatus+" to DB in Trade Service" );
        tradeService.findById ( tradeStatus.getId () );
        trade.setTradeStatus ( tradeStatus.getTradeStatus () );
        tradeService.save ( trade );
    }

    private void sendDataToQueue( Trade savedTrade){
        TradeStatus tradeStatus = new TradeStatus (  );
        tradeStatus.setId (savedTrade.getId ());
        tradeStatus.setTradeStatus ( savedTrade.getTradeStatus () );
        amqpTemplate.convertAndSend ( exchange, routingKey, tradeStatus);
    }

    private
    URI getUriOfTrade (Trade savedTrade) {
        return ServletUriComponentsBuilder.fromCurrentRequest ()
                .path ( "/{id}" )
                .buildAndExpand ( savedTrade.getId ())
                .toUri ();
    }
}
