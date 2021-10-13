package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    private static final String COUNT_USERS = "SELECT COUNT(u) FROM User u";
    private static final String GET_ALL_USERS = "SELECT u FROM User u";

    @PersistenceContext
    private final EntityManager manager;

    @Autowired
    public UserDao(EntityManager manager) {
        this.manager = manager;
    }

    public Optional<User> findById(long id) {
        return Optional.ofNullable(manager.find(User.class, id));
    }

    public List<User> findAll(Integer page, Integer pageSize) {
        return page != null ? manager.createQuery(GET_ALL_USERS, User.class).setFirstResult(page).setMaxResults(pageSize).getResultList()
                    : manager.createQuery(GET_ALL_USERS, User.class).getResultList();
    }

    public Integer countUsers() {
        return Integer.parseInt(manager.createQuery(COUNT_USERS).getSingleResult().toString());
    }
}
