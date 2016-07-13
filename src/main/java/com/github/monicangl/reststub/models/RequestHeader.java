package com.github.monicangl.reststub.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RequestHeader {
    @JsonIgnore
    @ManyToOne
    private Schema schema;

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    RequestHeader() { // jpa only
    }

    public RequestHeader(Schema schema, String name, String value) {
        this.schema = schema;
        this.name = name;
        this.value = value;
    }

    public String name;
    public String value;

    public Long getId() {
        return id;
    }

    public Schema getSchema() {
        return schema;
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