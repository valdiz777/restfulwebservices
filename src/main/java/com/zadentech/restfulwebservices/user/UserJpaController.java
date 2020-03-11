package com.zadentech.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.zadentech.restfulwebservices.exceptions.DataOperationException;
import com.zadentech.restfulwebservices.exceptions.PostNotFoundException;
import com.zadentech.restfulwebservices.exceptions.UserNotFoundException;
import com.zadentech.restfulwebservices.posts.Post;
import com.zadentech.restfulwebservices.posts.PostRepository;

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
public class UserJpaController {

    @Autowired
    private UserRepositoryV2 userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping(value = "/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(value = "/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("id=" + id);
        }
        EntityModel<User> model = new EntityModel<User>(optionalUser.get()); 
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());

        model.add(linkTo.withRel("all-users"));
        
        return model;
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("id=" + id);
        }

        log.info("user found {} ", id);
        userRepository.deleteById(id);
    }

    @GetMapping(value = "/jpa/users/{id}/posts")
    public List<Post> retrieveAllPosts(@PathVariable int id) {
        if (!userRepository.findById(id).isPresent()) {
            log.info("user not found {} ", id);
            throw new UserNotFoundException("id=" + id);
        }

        return userRepository.findById(id).get().getPosts();
    }

    @GetMapping(value = "/jpa/users/{id}/posts/{postId}")
    public Post retrieveSpecificUserPost(@PathVariable int id, @PathVariable int postId) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            log.info("user not found {} ", id);
            throw new UserNotFoundException("id=" + id);
        }

        Post post = optionalUser.get().getPosts().stream()
                                   .filter(aPost -> aPost.getId() == postId)
                                   .findFirst()
                                   .orElseThrow(() -> new PostNotFoundException("postId=" + postId));
        return post;
    }

    @PostMapping(value = "/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            log.info("user not found {} ", id);
            throw new UserNotFoundException("id=" + id);
        }

        post.setUser(optionalUser.get());
        postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}/posts").buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}