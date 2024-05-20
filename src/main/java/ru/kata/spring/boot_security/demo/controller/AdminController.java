package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public String showAllUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        //model.addAttribute("formUser", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "index";
    }

    @GetMapping("/new")
    public String addNewUser(Model model, Principal principal, User user) {
        model.addAttribute("thisUser", userService.getUserByEmail(principal.getName()));
        //model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "new-user";
    }

    @PostMapping("/saveAddUser")
    public String saveAddUser(@ModelAttribute("user") @Valid User user, BindingResult result, Model model, Principal principal,
            @RequestParam("selectedRoles") Long[] selectRoles) {

        userValidator.validate(user, result);
        if (result.hasErrors()) {
            model.addAttribute("thisUser", userService.getUserByEmail(principal.getName()));
            //model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getAllRoles());
            return "new-user";
        } else {
            Set<Role> rolesByArrayIds = roleService.getRolesByArrayIds(selectRoles);
            userService.addUser(user, rolesByArrayIds);
            return "redirect:/admin";
        }
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