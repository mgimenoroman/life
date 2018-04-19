package com.mgr.life.item;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mgr.life.RestEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ROUND_HALF_EVEN;

@Entity
@JsonDeserialize(as = Item.class)
public class Item extends RestEntity {

    private String name, type;
    private BigDecimal price;

    public Item() {
    }

    public Item(Long id, String name, String type, BigDecimal price) {
        super(id);
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public Item(String name, String type, BigDecimal price) {
        this.name = name;
        this.type = type;
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        boolean result = Objects.equals(name, item.name) &&
                Objects.equals(type, item.type);

        if (result) {
            if (price != null && item.price != null) {
                return Objects.equals(price.setScale(2, ROUND_HALF_EVEN), item.price.setScale(2, ROUND_HALF_EVEN));
            } else {
                return Objects.equals(price, item.price);
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, price);
    }
}
