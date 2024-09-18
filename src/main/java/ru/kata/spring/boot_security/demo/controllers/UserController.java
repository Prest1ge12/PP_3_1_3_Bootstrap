package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserSecurityDetails;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
//@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getAdminPanel(Model model) {
        getAuth(model);
        System.out.println("ПОЛУЧЕННЫЙ ID: " + userService); // Логирование id
        model.addAttribute("user", userService);
        return "admin";
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        getAuth(model);
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }


    @GetMapping("/user")
    public String getUser(Model model) {
        getAuth(model);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurityDetails userDetails = (UserSecurityDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        model.addAttribute("user", currentUser);
        return "user";
    }

    @GetMapping("/admin/new")
    public String newUser(Model model) {
        getAuth(model);
        model.addAttribute("user", new User());
        return "admin/new";
    }

    @PostMapping("/admin/new/create")
    public String createUser(@ModelAttribute("user") User user, Model model) {
        getAuth(model);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        getAuth(model);
        model.addAttribute("user", userService.getUser(id));
        return "admin/edit";
    }

    @PostMapping("/admin/update")
    public String update(@ModelAttribute("user") User user, Model model) {
        getAuth(model);
        userService.update(user.getId(), user);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/delete")
    public String delete(@ModelAttribute("user") User user, Model model) {
        getAuth(model);
        userService.delete(user.getId());
        return "redirect:/admin/users";

    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/index";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/login";
    }

    @GetMapping("/")
    public String index(Model model) {
        getAuth(model);
        return "index";
    }

    private void getAuth(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("isAuthenticated", auth != null && auth.isAuthenticated());
    }
}