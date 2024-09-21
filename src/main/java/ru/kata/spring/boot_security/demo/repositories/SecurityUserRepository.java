package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;

@Repository
public interface SecurityUserRepository extends JpaRepository<User, Long> {
//    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
//    Optional<User> findByUsername(@Param("username") String username);

    // Поиск в базе по Email
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.userEmail = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
