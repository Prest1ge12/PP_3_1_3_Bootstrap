package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAdminPanel(@AuthenticationPrincipal User autUser,
                                Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("editUser", new User());
        model.addAttribute("autUser", autUser);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("availableRoles", roleService.getAvailableRoles());
        boolean hasAdminRole = autUser.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getRoleName()));
        if (hasAdminRole) {
            return "/admin/admin";
        }
        return "/user/user";
    }

    @GetMapping("/user")
    public String getUserProfile(@AuthenticationPrincipal User autUser, Model model) {// Получаем пользователя по имени
        model.addAttribute("autUser", autUser);
        return "/admin/user";
    }


    @PostMapping("/new/create")
    public String createUser(@ModelAttribute("newUser") User newUser,
                             @RequestParam(value = "setRoles", required = false) Set<Long> roles) {
        userService.saveUser(newUser, roles);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") Long id,
                         @ModelAttribute("editUser") User user,
                         @RequestParam(value = "editRoles", required = false) Set<Long> roles) {
        userService.updateUser(id, user, roles);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin";

    }
}
