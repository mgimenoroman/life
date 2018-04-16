package com.mgr.life.item;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Item {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String type;
    private BigDecimal price;

    public Item() {
    }

    public Item(Long id, String name, String type, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public Item(String name, String type, BigDecimal price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Item setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Item setType(String type) {
        this.type = type;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Item setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
