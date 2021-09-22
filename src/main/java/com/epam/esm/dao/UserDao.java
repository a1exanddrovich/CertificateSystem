package com.epam.esm.dao;

import com.epam.esm.entity.User;
import com.epam.esm.sql.SqlQueries;
import com.epam.esm.utils.QueryConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserDao {

    private final EntityManager manager;
    private final QueryConstructor constructor;

    @Autowired
    public UserDao(EntityManager manager, QueryConstructor constructor) {
        this.constructor = constructor;
        this.manager = manager;
    }

    public Optional<User> findById(long id) {
        List<?> result = manager.createNativeQuery(SqlQueries.FIND_USER_BY_ID, User.class).setParameter(1, id).getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of((User) result.get(0));
    }

    public List<User> findAll(Integer page, Integer pageSize) {
        List<?> tags = manager.createNativeQuery(constructor.constructPaginatedQuery(page, pageSize, SqlQueries.FIND_ALL_USERS), User.class).getResultList();
        return tags.stream().map(User.class::cast).collect(Collectors.toList());
    }


    public void updateBalanceById(long userId, BigDecimal updatedBalance) {
        manager.getTransaction().begin();
        manager.createNativeQuery(SqlQueries.UPDATE_USER_BALANCE, User.class).setParameter(1, updatedBalance).setParameter(2, userId).executeUpdate();
        manager.getTransaction().commit();
    }

    public Integer countUsers() {
        return ((BigInteger) manager.createNativeQuery(SqlQueries.COUNT_ALL_USERS).getSingleResult()).intValue();
    }
}
