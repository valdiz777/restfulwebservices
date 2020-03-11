package com.zadentech.restfulwebservices.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * UserRepository
 */
@Repository
public interface UserRepositoryV2 extends JpaRepository<User, Integer> {

}