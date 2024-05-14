package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();

    Role getById(Long id);

    void deleteRole(Role role);

    void updateRole(Role role);

    void addNewRole(Role role);

    Set<Role> getRolesByArrayIds(Long... idRoles);
}
