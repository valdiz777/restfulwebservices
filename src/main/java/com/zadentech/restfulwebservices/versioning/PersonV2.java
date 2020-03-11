package com.zadentech.restfulwebservices.versioning;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Post
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonV2 {

    @Size(min=2, message="Name should have at least 2 characters")
    private Name name;
}