package com.github.monicangl.reststub.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RequestParameter {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    RequestParameter() { // jpa only
    }

    public RequestParameter(String name, String value) {
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
        RequestParameter parameter = (RequestParameter)obj;
        return this.name.equals(parameter.name) && this.value.equals(parameter.value);
    }

    public int hashCode() {
        return this.name.hashCode() * this.value.hashCode();
    }

}
