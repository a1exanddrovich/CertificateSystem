package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
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
public class TagDao implements EntityDao<Tag> {

    private final EntityManager manager;
    private final QueryConstructor constructor;

    @Autowired
    public TagDao(EntityManager manager, QueryConstructor constructor) {
        this.manager = manager;
        this.constructor = constructor;
    }

    @Override
    public long create(Tag tag) {
        manager.getTransaction().begin();
        manager.persist(tag);
        manager.getTransaction().commit();
        return tag.getId();
    }

    @Override
    public void deleteById(long id) {
        manager.getTransaction().begin();
        manager.createNativeQuery(SqlQueries.DELETE_TAG).setParameter(1, id).executeUpdate();
        manager.getTransaction().commit();
    }

    @Override
    public Optional<Tag> findById(long id) {
        List<?> result = manager.createNativeQuery(SqlQueries.FIND_TAG_BY_ID, Tag.class).setParameter(1, id).getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of((Tag) result.get(0));
    }

    public List<Tag> findAll(Integer page, Integer pageSize) {
        List<?> tags = manager.createNativeQuery(constructor.constructPaginatedQuery(page, pageSize, SqlQueries.FIND_ALL_TAGS), Tag.class).getResultList();
        return tags.stream().map(Tag.class::cast).collect(Collectors.toList());
    }

    public Optional<Tag> findTagByName(String tagName) {
        List<?> result = manager.createNativeQuery(SqlQueries.FIND_TAG_BY_NAME, Tag.class).setParameter(1, tagName).getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of((Tag) result.get(0));
    }

    public Integer countTags() {
        return ((BigInteger) manager.createNativeQuery(SqlQueries.COUNT_ALL_TAGS).getSingleResult()).intValue();
    }

    public Tag getMostPopular() {
        return (Tag) manager.createNativeQuery(SqlQueries.GET_MOST_POPULAR_TAG, Tag.class).getSingleResult();
    }

}
