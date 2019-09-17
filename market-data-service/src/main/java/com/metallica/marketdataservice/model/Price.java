package com.metallica.marketdataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Immutable
public final
class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
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
