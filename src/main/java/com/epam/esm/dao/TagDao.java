package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDao {

    private static final String GET_ALL_TAGS = "SELECT t FROM Tag t";
    private static final String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name=:tagName";
    private static final String TAG_NAME_PARAMETER = "tagName";
    private static final String COUNT_TAGS = "SELECT COUNT(t) FROM Tag t";
    private static final String GET_MOST_POPULAR_TAG = "select *\n" +
            "from tag\n" +
            "where id = (select tag_id\n" +
            "\t    from (select tag_id, count(tag_id) count from\n" +
            "\t\t\t\t\t\t     (select gift_certificate_id\n" +
            "\t\t\t\t\t\t      from user u\n" +
            "\t\t\t\t\t\t      join orders o\n" +
            "\t\t\t\t\t\t      on u.id=o.user_id\n" +
            "\t\t\t\t\t\t      where user_id = (select id\n" +
            "\t\t\t\t\t\t\t\t\t      from (select u.id, u.name, sum(o.price) as sum\n" +
            "\t\t\t\t\t\t\t\t\t\t    from user u\n" +
            "\t\t\t\t\t\t\t\t\t\t    join orders o\n" +
            "\t\t\t\t\t\t\t\t\t            on u.id=o.user_id\n" +
            "\t\t\t\t\t\t\t\t\t\t    group by u.id) s\n" +
            "\t\t\t\t\t\t\t\t\torder by s.sum desc limit 1)\n" +
            "\t\t\t\t\t\t      ) h\n" +
            "\t    join gift_certificate_tag gct\n" +
            "\t    on gct.gift_certificate_id = h.gift_certificate_id\n" +
            "\t    GROUP BY tag_id limit 1) s\n" +
            "\t   );\n";

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
        return page != null ? manager.createQuery(GET_ALL_TAGS, Tag.class).setFirstResult(page).setMaxResults(pageSize).getResultList()
                            : manager.createQuery(GET_ALL_TAGS, Tag.class).getResultList();
    }

    public Optional<Tag> findTagByName(String tagName) {
        return manager.createQuery(GET_TAG_BY_NAME, Tag.class).setParameter(TAG_NAME_PARAMETER, tagName).getResultStream().findFirst();
    }

    public Integer countTags() {
        return Integer.parseInt(manager.createQuery(COUNT_TAGS).getSingleResult().toString());
    }

    public Tag getMostPopular() {
        return (Tag) manager.createNativeQuery(GET_MOST_POPULAR_TAG, Tag.class).getSingleResult();
    }

}
