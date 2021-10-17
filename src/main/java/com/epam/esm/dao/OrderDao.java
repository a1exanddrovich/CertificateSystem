package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.query.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    private static final String USER_ID_PARAMETER = "user";

    @PersistenceContext
    private final EntityManager manager;

    @Autowired
    public OrderDao(EntityManager manager) {
        this.manager = manager;
    }

    public long create(Order order) {
        manager.persist(order);
        return order.getId();
    }

    public Optional<Order> findById(long id) {
        return Optional.ofNullable(manager.find(Order.class, id));
    }

    public List<Order> findAllByUserId(User user, Integer page, Integer pageSize) {
        return page != null ? manager.createQuery(Queries.GET_ORDERS_BY_USER_ID, Order.class).setParameter(USER_ID_PARAMETER, user).setFirstResult(page).setMaxResults(pageSize).getResultList()
                            : manager.createQuery(Queries.GET_ORDERS_BY_USER_ID, Order.class).setParameter(USER_ID_PARAMETER, user).getResultList();
    }

    public Integer countById(User user) {
        return Integer.parseInt(manager.createQuery(Queries.COUNT_ORDERS_BY_USER_ID).setParameter(USER_ID_PARAMETER, user).getSingleResult().toString());
    }

}