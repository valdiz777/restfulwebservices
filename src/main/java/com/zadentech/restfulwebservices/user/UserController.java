package com.zadentech.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.zadentech.restfulwebservices.exceptions.DataOperationException;
import com.zadentech.restfulwebservices.exceptions.PostNotFoundException;
import com.zadentech.restfulwebservices.exceptions.UserNotFoundException;
import com.zadentech.restfulwebservices.posts.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * UserResource
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(value = "/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("id=" + id);
        }
        EntityModel<User> model = new EntityModel<User>(user); 
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());

        model.add(linkTo.withRel("all-users"));
        
        return model;
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        if (userRepository.findOne(id) == null) {
            throw new UserNotFoundException("id=" + id);
        }

        log.info("user found {} ", id);
        Boolean removalSuccessful = userRepository.deleteById(id);

        if (removalSuccessful) {
            return ResponseEntity.ok().build();
        } else {
            throw new DataOperationException("Unable to remove user " + id);
        }
    }

    @GetMapping(value = "/users/{id}/posts")
    public List<Post> retrieveAllPosts(@PathVariable int id) {
        if (userRepository.findOne(id) == null) {
            log.info("user not found {} ", id);
            throw new UserNotFoundException("id=" + id);
        }

        return userRepository.findAllPosts(id);
    }

    @GetMapping(value = "/users/{id}/posts/{postId}")
    public Post retrieveSpecificUserPost(@PathVariable int id, @PathVariable int postId) {
        User user = userRepository.findOne(id);
        if (user == null) {
            log.info("user not found {} ", id);
            throw new UserNotFoundException("id=" + id);
        }

        Post post = user.getPosts().stream()
                                   .filter(aPost -> aPost.getId() == postId)
                                   .findFirst()
                                   .orElseThrow(() -> new PostNotFoundException("postId=" + postId));
        return post;
    }

    @PostMapping(value = "/users/{id}/posts")
    public ResponseEntity<Object> createPost(@PathVariable int id, @Valid @RequestBody Post post) {
        if (userRepository.findOne(id) == null) {
            log.info("user not found {} ", id);
            throw new UserNotFoundException("id=" + id);
        }

        int postId = userRepository.addPost(id, post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}/posts").buildAndExpand(postId)
                .toUri();

        return ResponseEntity.created(location).build();

    }
}