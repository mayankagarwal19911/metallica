package com.metallica.tradeservice.model;

import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Immutable
public final
class Price {

    private int id;
    private String commodity;
    private BigDecimal price;

    public
    Price (int id , String commodity , BigDecimal price) {
        this.id = id;
        this.commodity = commodity;
        this.price = price;
    }

    public
    int getId ( ) {
        return id;
    }

    public
    String getCommodity ( ) {
        return commodity;
    }

    public
    BigDecimal getPrice ( ) {
        return price;
    }

    @Override
    public
    String toString ( ) {
        return "Price{" +
                "id=" + id +
                ", commodity='" + commodity + '\'' +
                ", price=" + price +
                '}';
    }

    private
    Price ( ) {
    }
}
