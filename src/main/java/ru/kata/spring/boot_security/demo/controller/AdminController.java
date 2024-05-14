package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String showAllUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        model.addAttribute("formUser", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "index";
    }

    @GetMapping("/new")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listRoles", roleService.getAllRoles());
        return "index";
    }

    @PostMapping("/saveAddUser")
    public String saveAddUser(@Valid @ModelAttribute("user") User user
            , @RequestParam("selectedRoles") Long[] selectRoles
            , BindingResult result) {

        if (!result.hasErrors()) {
            Set<Role> rolesByArrayIds = roleService.getRolesByArrayIds(selectRoles);
            userService.addUser(user, rolesByArrayIds);
        }
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String updateUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("listRoles", roleService.getAllRoles());
        return "index";
    }

    @PostMapping("/{id}")
    public String saveUpUser(@Valid @ModelAttribute("user") User user
            , @RequestParam("selectedRoles") Long[] selectRoles
            , BindingResult result) {

        if (!result.hasErrors()) {
            Set<Role> rolesByArrayIds = roleService.getRolesByArrayIds(selectRoles);
            userService.updateUser(user, rolesByArrayIds);
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(userService.getById(id));
        return "redirect:/admin";
    }
}