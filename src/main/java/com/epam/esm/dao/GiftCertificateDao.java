package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.sql.SqlQueries;
import com.epam.esm.utils.Constructor;
import com.epam.esm.utils.QueryConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateDao implements EntityDao<GiftCertificate> {

    private final EntityManager manager;
    private final QueryConstructor constructor;
    private final Constructor<GiftCertificate> searchCriteriaConstructor;

    @Autowired
    public GiftCertificateDao(EntityManager manager, QueryConstructor constructor, Constructor<GiftCertificate> searchCriteriaConstructor) {
        this.manager = manager;
        this.constructor = constructor;
        this.searchCriteriaConstructor = searchCriteriaConstructor;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
//        manager.getTransaction().begin();
        manager.createNativeQuery(SqlQueries.DELETE_CERTIFICATE).setParameter(1, id).executeUpdate();
//        manager.getTransaction().commit();
    }

    @Override
    @Transactional
    public long create(GiftCertificate giftCertificate) {
//        manager.getTransaction().begin();
        manager.persist(giftCertificate);
//        manager.getTransaction().commit();
        return giftCertificate.getId();
    }

    public List<GiftCertificate> getGiftCertificates(String[] tagNames, String giftCertificateName, String description, String sortByName, String sortByDate, Integer page, Integer pageSize) {
//        String query = constructor.constructGiftCertificateQuery(tagNames, giftCertificateName, description, sortByName, sortByDate, page, pageSize);
//        List<?> tags = manager.createNativeQuery(query, GiftCertificate.class).getResultList();
//
//        return tags.stream().map(GiftCertificate.class::cast).collect(Collectors.toList());
        CriteriaQuery<GiftCertificate> criteriaQuery = searchCriteriaConstructor.constructGiftCertificateQuery(manager, GiftCertificate.class, tagNames, giftCertificateName, description, sortByName, sortByDate, page, pageSize);
        List<GiftCertificate> result = manager.createQuery(criteriaQuery).getResultList();
        return result;
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        manager.clear();
//        List<?> result = manager.createNativeQuery(SqlQueries.FIND_CERTIFICATE_BY_ID, GiftCertificate.class).setParameter(1, id).getResultList();
//        return result.isEmpty() ? Optional.empty() : Optional.of((GiftCertificate) result.get(0));
        CriteriaQuery<GiftCertificate> criteriaQuery = searchCriteriaConstructor.constructFindByIdQuery(manager, GiftCertificate.class, id);
        List<GiftCertificate> result = manager.createQuery(criteriaQuery).getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Transactional
    public void updateGiftCertificate(long id, GiftCertificate giftCertificate) {
        String updateQuery = constructor.constructGiftCertificateUpdateQuery(id, giftCertificate);
//        manager.getTransaction().begin();
        manager.createNativeQuery(updateQuery).executeUpdate();
//        manager.getTransaction().commit();
    }

    public Integer countGiftCertificates() {
        return ((BigInteger) manager.createNativeQuery(SqlQueries.COUNT_ALL_CERTIFICATES).getSingleResult()).intValue();
    }
}
