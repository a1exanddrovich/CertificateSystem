package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.sql.SqlQueries;
import com.epam.esm.utils.QueryConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderDao {

    private final EntityManager manager;
    private final QueryConstructor constructor;

    @Autowired
    public OrderDao(EntityManager manager, QueryConstructor constructor) {
        this.constructor = constructor;
        this.manager = manager;
    }

    public long create(Order order) {
        manager.getTransaction().begin();
        manager.persist(order);
        manager.getTransaction().commit();
        return order.getId();
    }

    public Optional<Order> findById(long id) {
        List<?> result = manager.createNativeQuery(SqlQueries.FIND_ORDER_BY_ID, Order.class).setParameter(1, id).getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of((Order) result.get(0));
    }

    public List<Order> findOrdersByUserId(long id, Integer page, Integer pageSize) {
        List<?> tags = manager.createNativeQuery(constructor.constructPaginatedQuery(page, pageSize, SqlQueries.FIND_ALL_USER_ORDERS), Order.class).setParameter(1, id).getResultList();
        return tags.stream().map(Order.class::cast).collect(Collectors.toList());
    }

    public void deleteByGiftCertificateId(long id) {
        manager.getTransaction().begin();
        manager.createNativeQuery(SqlQueries.DELETE_ORDER).setParameter(1, id).executeUpdate();
        manager.getTransaction().commit();
    }

    public Integer countOrdersById(long id) {
        return ((BigInteger) manager.createNativeQuery(SqlQueries.COUNT_USER_ORDERS).setParameter(1, id).getSingleResult()).intValue();
    }

}
