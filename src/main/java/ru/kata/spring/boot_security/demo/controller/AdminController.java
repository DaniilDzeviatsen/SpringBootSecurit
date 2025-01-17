package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsServiceImpl;
import ru.kata.spring.boot_security.demo.security.UserPrincipal;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final UserDetailsServiceImpl userDetailsService;

    private final RoleDao roleDao;

    @Autowired
    public AdminController(UserService userService, UserDetailsServiceImpl userDetailsService, RoleDao roleDao) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.roleDao = roleDao;
    }

    @ModelAttribute("listRoles")
    public List<Role> listRoles() {
        return roleDao.findAll();
    }


    @GetMapping("/user")
    public String getUserById(Model model, Authentication authentication) {
        String username = authentication.getName();
        UserPrincipal user = (UserPrincipal) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "user";
    }


    @GetMapping()
    public String getUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("principal", userDetailsService.loadUserByUsername(principal.getName()));
        return "users";
    }

    @GetMapping("/")
    public String getUserByID(@RequestParam Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "show";
    }

    @GetMapping("/new")
    public String showSignUpForm(@ModelAttribute("user") User user, Model model, Principal principal) {
        model.addAttribute("principal", userDetailsService.loadUserByUsername(principal.getName()));
        return "new";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}

