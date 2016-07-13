package com.github.monicangl.reststub.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RequestHeader {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    RequestHeader() { // jpa only
    }

    public RequestHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name;
    public String value;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(Object obj) {
        RequestHeader header = (RequestHeader) obj;
        return this.name.equals(header.name) && this.value.equals(header.value);
    }

    public int hashCode() {
        return this.name.hashCode() * this.value.hashCode();
    }
}