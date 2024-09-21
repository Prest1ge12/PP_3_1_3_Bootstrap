package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserSecurityDetails;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;


@Controller
//@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin")
    public String getAdminPanel(Model model, Principal principal) {
        getAuth(model);
        model.addAttribute("users", userService.getAllUsers());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurityDetails userDetails = (UserSecurityDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        model.addAttribute("currenUser", currentUser);
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
//        getAuth(model);
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
        model.addAttribute("newUser", new User());
        return "admin/new";
    }

    @PostMapping("/admin/new/create")
    public String createUser(@ModelAttribute("newUser") User user,Model model) {
        try {
            userService.saveUser(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Пользователь с таким Email уже существует");
            return "admin";
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        getAuth(model);
        model.addAttribute("user", userService.getUser(id));
        return "admin/edit";
    }

    @PostMapping("/admin/update")
    public String update(@ModelAttribute("user") User user) {
        userService.update(user.getId(), user);
        return "redirect:/admin";
    }


    @PostMapping("/admin/delete")
    public String delete(@ModelAttribute("user") User user, Model model) {
        getAuth(model);
        userService.delete(user.getId());
        return "redirect:/admin";

    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/index";
    }


    @GetMapping("/index")
    public String index(Model model) {
        getAuth(model);
        return "index";
    }

    @GetMapping("/")
    public String redirectToIndex(Model model) {
        getAuth(model);
        return "redirect:index";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration/reg")
    public String regUser(@ModelAttribute("user") User user, Model model) {
        getAuth(model);

        System.out.println("Регистрация пользователя: " + user.getUsername());

        // Инициализируем роли, если они не заданы
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        // Создаем роль и присваиваем её пользователю
        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        user.getRoles().add(userRole);


        // Хэшируем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        // Логируем роли пользователя
        System.out.println("Назначенные роли: " + user.getRoles());
        System.out.println("Пользователь успешно сохранен: " + user);


        // Здесь нужно выполнить авторизацию
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("Авторизация пользователя: " + user.getUsername());
        System.out.println("Пользователь аутентифицирован: " + user.getUsername());

        return "redirect:/user";
    }

    private void getAuth(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("isAuthenticated", auth != null && auth.isAuthenticated());
    }
}