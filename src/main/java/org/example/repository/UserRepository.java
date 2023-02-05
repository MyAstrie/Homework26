package org.example.repository;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public Optional<User> findByLogin(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst();
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return users.stream().filter(user -> user.getLogin().equals(login) && user.getPassword().equals(password)).findFirst();
    }

    public void addUser(User user) {
        users.add(user);
    }
}
