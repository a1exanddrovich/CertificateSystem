package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.utils.Constructor;
import com.epam.esm.utils.QueryConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDao {

    private static final String COUNT_CERTIFICATES = "SELECT COUNT(c) FROM GiftCertificate c";

    @PersistenceContext
    private final EntityManager manager;
    private final QueryConstructor constructor;
    private final Constructor<GiftCertificate> searchCriteriaConstructor;

    @Autowired
    public GiftCertificateDao(EntityManager manager, QueryConstructor constructor, Constructor<GiftCertificate> searchCriteriaConstructor) {
        this.manager = manager;
        this.constructor = constructor;
        this.searchCriteriaConstructor = searchCriteriaConstructor;
    }

    public void deleteById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = findById(id);
        optionalGiftCertificate.ifPresent(manager::remove);
    }

    public long create(GiftCertificate giftCertificate) {
        manager.persist(giftCertificate);
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

    public Optional<GiftCertificate> findById(long id) {
        return Optional.ofNullable(manager.find(GiftCertificate.class, id));
    }

    public void updateGiftCertificate(long id, GiftCertificate giftCertificate) {
        String updateQuery = constructor.constructGiftCertificateUpdateQuery(id, giftCertificate);
//        manager.getTransaction().begin();
        manager.createNativeQuery(updateQuery).executeUpdate();
//        manager.getTransaction().commit();
    }

    public Integer countGiftCertificates() {
        return Integer.parseInt(manager.createQuery(COUNT_CERTIFICATES).getSingleResult().toString());
    }
}
