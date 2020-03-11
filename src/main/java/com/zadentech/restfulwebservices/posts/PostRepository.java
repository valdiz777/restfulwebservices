package com.zadentech.restfulwebservices.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * UserRepository
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

}