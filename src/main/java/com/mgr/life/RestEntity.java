package com.mgr.life;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonDeserialize(using = RestEntityDeserializer.class)
public abstract class RestEntity {

    @Id
    @GeneratedValue
    private Long id;

    public RestEntity() {
    }

    public RestEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public RestEntity setId(Long id) {
        this.id = id;
        return this;
    }
}
