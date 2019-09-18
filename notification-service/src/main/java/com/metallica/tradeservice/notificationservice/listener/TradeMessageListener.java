package com.metallica.tradeservice.notificationservice.listener;

import com.metallica.tradeservice.notificationservice.client.TradeServiceClient;
import com.metallica.tradeservice.notificationservice.model.TradeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@EnableRabbit
@Component
public
class TradeMessageListener implements Serializable {

    private static
    Logger loggerFactory = LoggerFactory.getLogger ( TradeMessageListener.class );

    @Autowired
    private
    TradeServiceClient tradeServiceClient;

    @RabbitListener(queues = "${rabbitmq.queue-name}")
    public void receivedMessage(TradeStatus trade) {
        loggerFactory.info ("******************Received trade is: {}", trade);
        trade.setTradeStatus("processed");
        TradeStatus req = new TradeStatus();
        req.setId (trade.getId ());
        req.setTradeStatus ( trade.getTradeStatus () );
        tradeServiceClient.updateTrade(req);
    }
//
//    @RabbitListener(queues="${rabbitmq.queue-name}")
//    public void receivedMessage(TradeStatus message) {
//        loggerFactory.info ("******************Received trade is: {}", message);
//    }

//    @RabbitListener(queues="${rabbitmq.queue-name}")
//    public void receivedMessage(TradeStatus msg) {
//        System.out.println("Received Message: " + msg);
//    }
}
