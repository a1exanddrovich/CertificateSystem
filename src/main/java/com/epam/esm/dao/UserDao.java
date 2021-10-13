package com.epam.esm.dao;

import com.epam.esm.entity.User;
import com.epam.esm.sql.SqlQueries;
import com.epam.esm.utils.QueryConstructor;
import com.epam.esm.utils.Constructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserDao {

    private final EntityManager manager;
    private final QueryConstructor constructor;
    private final Constructor<User> searchCriteriaConstructor;

    @Autowired
    public UserDao(EntityManager manager, QueryConstructor constructor, Constructor<User> searchCriteriaConstructor) {
        this.constructor = constructor;
        this.manager = manager;
        this.searchCriteriaConstructor = searchCriteriaConstructor;
    }

    public Optional<User> findById(long id) {
//        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
//        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
//        Root<User> root = criteriaQuery.from(User.class);
//        List<Predicate> searchCriterias = new ArrayList<>();
//        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
//        List<?> result = manager.createNativeQuery(SqlQueries.FIND_USER_BY_ID, User.class).setParameter(1, id).getResultList();
        CriteriaQuery<User> criteriaQuery = searchCriteriaConstructor.constructFindByIdQuery(manager, User.class, id);
        List<User> result = manager.createQuery(criteriaQuery).getResultList();
//        return result.isEmpty() ? Optional.empty() : Optional.of((User) result.get(0));
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public List<User> findAll(Integer page, Integer pageSize) {
        List<?> tags = manager.createNativeQuery(constructor.constructPaginatedQuery(page, pageSize, SqlQueries.FIND_ALL_USERS), User.class).getResultList();
        return tags.stream().map(User.class::cast).collect(Collectors.toList());
    }


    @Transactional
    public void updateBalanceById(long userId, BigDecimal updatedBalance) {
//        manager.getTransaction().begin();
        manager.createNativeQuery(SqlQueries.UPDATE_USER_BALANCE, User.class).setParameter(1, updatedBalance).setParameter(2, userId).executeUpdate();
//        manager.getTransaction().commit();
    }

    public Integer countUsers() {
        return ((BigInteger) manager.createNativeQuery(SqlQueries.COUNT_ALL_USERS).getSingleResult()).intValue();
    }
}
