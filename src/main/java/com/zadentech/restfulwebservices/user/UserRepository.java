package com.zadentech.restfulwebservices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zadentech.restfulwebservices.posts.Post;

import org.springframework.stereotype.Repository;

/*
 * UserRepository
 */
@Repository
public class UserRepository {

    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3;

    static {
        users.add(new User(1, "Adam", new Date()));
        users.add(new User(2, "Eve", new Date()));
        users.add(new User(3, "Jack", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++usersCount);
        }
        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    public Boolean deleteById(int id) {
        Boolean removalSuccessful = false;
        for (User user : users) {
            if (user.getId() == id) {
                users.remove(user);
                --usersCount;
                removalSuccessful = true;
                break;
            }
        }
        return removalSuccessful;
    }

    public List<Post> findAllPosts(int id) {
        if (findOne(id) != null) {
            return findOne(id).getPosts();
        }
        return null;
    }

    public Integer addPost(int id, Post post) {
        User user = findOne(id);
        if (post.getId() == null) {
            post.setId(user.getPosts().size());
        }
        user.getPosts().add(post);
        return post.getId();
    }
}