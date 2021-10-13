package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.utils.QueryConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    private static final String COUNT_ORDERS_BY_USER_ID = "SELECT COUNT(o) FROM Order o WHERE o.user=:user";
    private static final String GET_ORDERS_BY_USER_ID = "SELECT o FROM Order o WHERE o.user=:user";
    private static final String USER_ID_PARAMETER = "user";

    @PersistenceContext
    private final EntityManager manager;

    @Autowired
    public OrderDao(EntityManager manager, QueryConstructor constructor) {
        this.manager = manager;
    }

    @Transactional
    public long create(Order order) {
        manager.persist(order);
        return order.getId();
    }

    public Optional<Order> findById(long id) {
        return Optional.ofNullable(manager.find(Order.class, id));
    }

    public List<Order> findAllByUserId(long id, Integer page, Integer pageSize) {
        return page != null ? manager.createQuery(GET_ORDERS_BY_USER_ID, Order.class).setParameter(USER_ID_PARAMETER, id).setFirstResult(page).setMaxResults(pageSize).getResultList()
                    : manager.createQuery(GET_ORDERS_BY_USER_ID, Order.class).setParameter(USER_ID_PARAMETER, id).getResultList();
    }

    public Integer countById(long id) {
        return Integer.parseInt(manager.createQuery(COUNT_ORDERS_BY_USER_ID).setParameter(USER_ID_PARAMETER, id).getSingleResult().toString());
    }

}
