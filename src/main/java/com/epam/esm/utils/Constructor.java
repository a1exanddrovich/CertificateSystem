package com.epam.esm.utils;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class Constructor<T> {

    public CriteriaQuery<T> constructFindByIdQuery(EntityManager manager, Class<T> typeClass, long id) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(typeClass);
        Root<T> root = criteriaQuery.from(typeClass);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        return criteriaQuery;
    }

    public CriteriaQuery<T> constructGiftCertificateQuery(EntityManager manager, Class<T> typeClass, String[] tagNames, String giftCertificateName, String description, String sortByName, String sortByDate, Integer page, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(typeClass);
        Root<T> root = criteriaQuery.from(typeClass);
        List<Predicate> criteria = new ArrayList<>();
        if (giftCertificateName != null) {
            criteria.add(criteriaBuilder.like(root.get("name"), "%" + giftCertificateName + "%"));
        }
        if (description != null) {
            criteria.add(criteriaBuilder.like(root.get("description"), "%" + description + "%"));
        }
        criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[criteria.size()])));
        if (sortByName != null && sortByDate == null) {
            if (sortByName.equals("asc")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
//                criteria.add((Predicate) criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name"))));
            } else if (sortByName.equals("desc")) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
//                query.append(ORDER_BY_NAME_STATEMENT).append(DESCENDING_ORDER);
            }
        }

        if (sortByDate != null && sortByName == null) {
            if (sortByDate.equals("asc")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("creationDate")));
//                query.append(ORDER_BY_DATE_STATEMENT);
            } else if (sortByDate.equals("desc")) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("creationDate")));
//                query.append(ORDER_BY_DATE_STATEMENT).append(DESCENDING_ORDER);
            }
        }

//        if (page != null && pageSize != null) {
////            query.append(LIMIT).append(pageSize * (page - 1)).append(COMMA).append(pageSize);
//            criteriaQuery.
//        }

        return criteriaQuery;
    }

}
