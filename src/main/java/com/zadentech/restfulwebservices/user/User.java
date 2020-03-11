package com.zadentech.restfulwebservices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.zadentech.restfulwebservices.posts.Post;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity
@ApiModel(description="All details about the user.")
public class User {

    private static final String BIRTHDATE_MESSAGE = "Date should be in the past";
    private static final String NAME_MESSAGE = "Name should have at least 2 characters";

    @Id
    @GeneratedValue
    private Integer id;
    @Size(min=2, message=NAME_MESSAGE)
    @ApiModelProperty(notes=NAME_MESSAGE)
    private String name;
    @Past(message=BIRTHDATE_MESSAGE)
    @ApiModelProperty(notes=BIRTHDATE_MESSAGE)
    private Date birthDate;

    @Getter
    @OneToMany(mappedBy="user")
    private final List<Post> posts = new ArrayList<>();
}