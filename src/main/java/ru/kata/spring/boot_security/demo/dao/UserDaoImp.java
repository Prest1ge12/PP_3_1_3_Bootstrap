package ru.kata.spring.boot_security.demo.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        String jpql = "SELECT u FROM User u";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        return query.getResultList();
    }

    @Override
    public User getUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        return user;
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                entityManager.persist(role);
            }
        }
    }

    @Override
    public void update(Long id, User updateUser) {
        User existingUser = entityManager.find(User.class, id);
        if (existingUser == null) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        existingUser.setUsername(updateUser.getUsername());
        existingUser.setAge(updateUser.getAge());
        existingUser.setRoles(updateUser.getRoles()); // Обновление ролей
        entityManager.merge(existingUser);
    }

    @Override
    public void delete(Long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        entityManager.remove(user);
    }


}