package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.SecurityUserRepository;
import ru.kata.spring.boot_security.demo.security.UserSecurityDetails;

import java.util.Optional;

@Service
public class SecurityServiceImp implements UserDetailsService {
    private final SecurityUserRepository securityUserRepository;

    @Autowired
    public SecurityServiceImp(SecurityUserRepository securityUserRepository) {
        this.securityUserRepository = securityUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = securityUserRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User '%s' не найден", username));
        }
        User user = userOpt.get();
//        Hibernate.initialize(user.getRoles());
        System.out.println("Загружен пользователь: " + user.getUsername() + " с ролями: " + user.getRoles());
        return new UserSecurityDetails(user);

    }
}
