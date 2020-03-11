package com.zadentech.restfulwebservices.filtering;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

//Static filtering = @JsonIgnoreProperties & @JsonIgnore
@Data
@AllArgsConstructor
//@JsonIgnoreProperties(value = {"field1", "field2"})
@JsonFilter("SomeBeanFilter") //Dynamic filtering
public class SomeBean {

    private String field1;
    private String field2;
   // @JsonIgnore //prevents field 3 from being showed in the GET call
    private String field3;

}
