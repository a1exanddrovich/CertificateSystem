package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.query.Queries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDao {

    private static final String TAG_NAME_PARAMETER = "tagName";

    @PersistenceContext
    private final EntityManager manager;

    @Autowired
    public TagDao(EntityManager manager) {
        this.manager = manager;
    }

    public long create(Tag tag) {
        manager.persist(tag);
        return tag.getId();
    }

    public void deleteById(long id) {
        Optional<Tag> optionalTag = findById(id);
        optionalTag.ifPresent(manager::remove);
    }

    public Optional<Tag> findById(long id) {
        return Optional.ofNullable(manager.find(Tag.class, id));
    }

    public List<Tag> findAll(Integer page, Integer pageSize) {
        return manager.createQuery(Queries.GET_ALL_TAGS, Tag.class).setFirstResult(page).setMaxResults(pageSize).getResultList();
    }

    public Optional<Tag> findTagByName(String tagName) {
        return manager.createQuery(Queries.GET_TAG_BY_NAME, Tag.class).setParameter(TAG_NAME_PARAMETER, tagName).getResultStream().findFirst();
    }

    public Integer countTags() {
        return Integer.parseInt(manager.createQuery(Queries.COUNT_TAGS).getSingleResult().toString());
    }

    public Tag getMostPopular() {
        return (Tag) manager.createNativeQuery(Queries.GET_MOST_POPULAR_TAG, Tag.class).getSingleResult();
    }

}
