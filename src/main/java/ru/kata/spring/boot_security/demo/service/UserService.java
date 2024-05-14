package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();

    void addUser(User user, Set<Role> rolesByArrayIds);

    void updateUser(User user, Set<Role> rolesByArrayIds);

    void deleteUser(User user);

    User getById(Long id);

    User getUserByEmail(String email);

}
