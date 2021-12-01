package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.query.Queries;
import com.epam.esm.utils.GiftCertificateCriteriaBuilder;
import com.epam.esm.utils.GiftCertificateQueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDao {

    @PersistenceContext
    private EntityManager manager;
    private final GiftCertificateCriteriaBuilder queryBuilder;

    @Autowired
    public GiftCertificateDao(GiftCertificateCriteriaBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Transactional
    public void deleteById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = findById(id);
        optionalGiftCertificate.ifPresentOrElse(manager::remove, EntityNotExistsException::new);
    }

    @Transactional
    public long create(GiftCertificate giftCertificate) {
        manager.persist(giftCertificate);
        return giftCertificate.getId();
    }

    public List<GiftCertificate> getGiftCertificates(GiftCertificateQueryParameters parameters, Integer page, Integer pageSize) {
        CriteriaQuery<GiftCertificate> criteriaQuery = queryBuilder.buildQuery(manager, parameters);
        return manager.createQuery(criteriaQuery).setFirstResult(page).setMaxResults(pageSize).getResultList();
    }

    public Optional<GiftCertificate> findById(long id) {
        return Optional.ofNullable(manager.find(GiftCertificate.class, id));
    }

    @Transactional
    public void updateGiftCertificate(GiftCertificate giftCertificate) {
        manager.merge(giftCertificate);
    }

    public Integer countGiftCertificates() {
        return Integer.parseInt(manager.createQuery(Queries.COUNT_CERTIFICATES).getSingleResult().toString());
    }
}
