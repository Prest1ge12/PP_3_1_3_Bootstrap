//package ru.kata.spring.boot_security.demo.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import ru.kata.spring.boot_security.demo.service.SecurityServiceImp;
//
//import java.util.Collections;
//
//@Component
//public class AuthProviderImp implements AuthenticationProvider {
//
//    private final SecurityServiceImp securityServiceImp;
//
//    @Autowired
//    public AuthProviderImp(SecurityServiceImp securityServiceImp) {
//        this.securityServiceImp = securityServiceImp;
//
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//
//        UserDetails userDetails = securityServiceImp.loadUserByUsername(username);
//
//        String password = authentication.getCredentials().toString();
//
//        if (!password.equals(userDetails.getPassword())) {
//            throw new BadCredentialsException("Пароль не верный");
//        }
//
//        return new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//}
