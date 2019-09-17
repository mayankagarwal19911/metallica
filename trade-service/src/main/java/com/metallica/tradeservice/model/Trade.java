package com.metallica.tradeservice.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public
class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String side;
    BigDecimal price;
    BigDecimal quantity;
    String commodity;
    String counterParty;
    String location;
    Date tradeDate;
    String tradeStatus;

    public
    Long getId ( ) {
        return id;
    }

    public
    void setId (Long id) {
        this.id = id;
    }

    public
    String getSide ( ) {
        return side;
    }

    public
    void setSide (String side) {
        this.side = side;
    }

    public
    BigDecimal getPrice ( ) {
        return price;
    }

    public
    void setPrice (BigDecimal price) {
        this.price = price;
    }

    public
    BigDecimal getQuantity ( ) {
        return quantity;
    }

    public
    void setQuantity (BigDecimal quantity) {
        this.quantity = quantity;
    }

    public
    String getCommodity ( ) {
        return commodity;
    }

    public
    void setCommodity (String commodity) {
        this.commodity = commodity;
    }

    public
    String getCounterParty ( ) {
        return counterParty;
    }

    public
    void setCounterParty (String counterParty) {
        this.counterParty = counterParty;
    }

    public
    String getLocation ( ) {
        return location;
    }

    public
    void setLocation (String location) {
        this.location = location;
    }

    public
    Date getTradeDate ( ) {
        return tradeDate;
    }

    public
    void setTradeDate (Date tradeDate) {
        this.tradeDate = tradeDate;
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
