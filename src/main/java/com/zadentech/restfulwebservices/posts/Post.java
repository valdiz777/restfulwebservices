package com.zadentech.restfulwebservices.posts;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zadentech.restfulwebservices.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Post
 */
@Data
@RequiredArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue
    @NotNull
    private Integer id;
    @Size(min=2, message="Name should have at least 2 characters")
    @NotNull
    private String title;
    @Size(min=2, message="Name should have at least 2 characters")
    @NotNull
    private String body;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JsonIgnore // this prevents a recursive loop as user will try to get post which will consequently try to get user...etc..you get the point
    private User user;
}