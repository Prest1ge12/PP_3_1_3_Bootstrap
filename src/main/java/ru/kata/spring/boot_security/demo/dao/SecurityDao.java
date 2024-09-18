package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

public class SecurityDao {
    @PersistenceContext
    private EntityManager entityManager;

    //Для поиска юзера в базе по имени
    public User findByUsername(String name) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("User not found with username: " + name);
        }
    }
}
