package com.epam.esm.dao;

import com.epam.esm.sql.SqlQueries;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateTagDao {

    private final EntityManager manager;

    @Autowired
    public GiftCertificateTagDao(EntityManager manager) {
        this.manager = manager;
    }

    public void addTagId(long id, Long tagId) {
        manager.getTransaction().begin();
        manager.createNativeQuery(SqlQueries.CONNECT_TAG_TO_CERTIFICATE).setParameter(1, id).setParameter(2, tagId).executeUpdate();
        manager.getTransaction().commit();
    }

    public void deleteTagId(long id, Long tagId) {
        manager.getTransaction().begin();
        manager.createNativeQuery(SqlQueries.DISCONNECT_TAG_TO_CERTIFICATE).setParameter(1, id).setParameter(2, tagId).executeUpdate();
        manager.getTransaction().commit();
    }

    public void createConnections(long giftCertificateId, Set<Tag> tags) {
        manager.getTransaction().begin();
        for (Tag tag : tags) {
            manager.createNativeQuery(SqlQueries.MAKE_TIDED_TAGS_AND_CERTIFICATIONS).setParameter(1, giftCertificateId).setParameter(2, tag.getName()).executeUpdate();
        }
        manager.getTransaction().commit();
    }

    public List<Long> getIdsBeforeUpdate(long id) {
        List<?> tags = manager.createNativeQuery(SqlQueries.SELECT_TAG_ID_BY_CERTIFICATE_ID).setParameter(1, id).getResultList();
        return tags.stream().map(x -> Long.valueOf(Long.parseLong(x.toString())).longValue()).collect(Collectors.toList());
    }

    public void deleteGiftCertificateById(long id) {
        manager.getTransaction().begin();
        manager.createNativeQuery(SqlQueries.MAKE_UNTIED_CERTIFICATE).setParameter(1, id).executeUpdate();
        manager.getTransaction().commit();
    }

    public void deleteByTagId(long id) {
        manager.getTransaction().begin();
        manager.createNativeQuery(SqlQueries.MAKE_UNTIED_TAG).setParameter(1, id).executeUpdate();
        manager.getTransaction().commit();
    }


}
