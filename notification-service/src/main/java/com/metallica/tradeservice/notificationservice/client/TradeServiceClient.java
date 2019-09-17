package com.metallica.tradeservice.notificationservice.client;

import com.metallica.tradeservice.notificationservice.model.TradeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public
class TradeServiceClient {

    @Autowired
    private
    RestTemplate restTemplate;

    public void updateTrade(TradeStatus request){
        restTemplate.put ( "http://zuul-api-gateway-server/metallica-trade-service/trade/update", request );
    }
}
