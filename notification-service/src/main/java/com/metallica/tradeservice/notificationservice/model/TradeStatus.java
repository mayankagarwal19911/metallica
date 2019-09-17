package com.metallica.tradeservice.notificationservice.model;

import java.io.Serializable;

public
class TradeStatus implements Serializable {


    Long id;
    String tradeStatus;

    public
    TradeStatus ( ) {
    }

    public
    TradeStatus (Long id , String tradeStatus) {
        this.id = id;
        this.tradeStatus = tradeStatus;
    }

    public
    Long getId ( ) {
        return id;
    }

    public
    void setId (Long id) {
        this.id = id;
    }

    public
    String getTradeStatus ( ) {
        return tradeStatus;
    }

    public
    void setTradeStatus (String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
}
