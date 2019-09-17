package com.metallica.tradeservice.controller;

import com.metallica.tradeservice.model.Price;
import com.metallica.tradeservice.model.Trade;
import com.metallica.tradeservice.rabbitmq.TradeStatus;
import com.metallica.tradeservice.service.ITrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private
    ITrade tradeService;

    @Autowired
    private
    RestTemplate restTemplate;


    @Autowired
    private
    RabbitTemplate rabbitTemplate;

    private Trade trade;

    @Value("${rabbitmq.exchange-name}")
    private String exchange;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @PostMapping("/buy")
    public
    ResponseEntity<Object> buyTrade(@RequestBody Trade trade){

        this.trade = trade;
        Price commodity = restTemplate.getForObject ( "http://zuul-api-gateway-server/market-data-service/mds/get-price/"+trade.getCommodity (), Price.class );

        loggerFactory.info ( " Commodity details : "+commodity );
        trade.setTradeStatus ( "initiated" );
        trade.setPrice ( commodity.getPrice () );
        Trade savedTrade = tradeService.save ( trade );

        loggerFactory.info ( "Working with RabbitMQ................" );
        TradeStatus tradeStatus = new TradeStatus (  );
        tradeStatus.setId (savedTrade.getId ());
        tradeStatus.setTradeStatus ( savedTrade.getTradeStatus () );
        rabbitTemplate.convertAndSend ( exchange, routingKey, tradeStatus);


        URI location = ServletUriComponentsBuilder.fromCurrentRequest ()
                .path ( "/{id}" )
                .buildAndExpand ( savedTrade.getId ())
                .toUri ();

        return ResponseEntity.created ( location ).build ();
    }


    @PutMapping("/update")
    public void updateTradeStatus(@RequestBody TradeStatus tradeStatus){
        loggerFactory.info ( "Updating {} "+tradeStatus+" to DB in Trade Service" );
        tradeService.findById ( tradeStatus.getId () );
        trade.setTradeStatus ( tradeStatus.getTradeStatus () );
        tradeService.save ( trade );
    }

}
