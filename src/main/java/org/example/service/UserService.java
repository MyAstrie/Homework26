package org.example.service;

import org.example.exceptions.UserNonUniqueException;
import org.example.model.User;
import org.example.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getAllUserLogins() {
        List<User> users = userRepository.getUsers();
        List<String> logins = new ArrayList<>();

        for (User user : users) {
            logins.add(user.getLogin());
        }

        return logins;
    }

    public void createNewUser(String login, String password) throws UserNonUniqueException {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Логин и пароль не могут быть пустыми или null");
        }

        Optional<User> existingUser = userRepository.findByLogin(login);
        if (existingUser.isPresent()) {
            throw new UserNonUniqueException("Пользователь с таким же логином уже существует");
        }

        User user = new User(login, password);
        userRepository.addUser(user);
    }

    public boolean checkUserLogin(String login, String password) {
        Optional<User> user = userRepository.findByLoginAndPassword(login, password);
        return user.isPresent();
    }
}
