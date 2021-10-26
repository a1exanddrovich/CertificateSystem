package com.epam.esm.utils;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateQueryException;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class GiftCertificateCriteriaBuilder {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAGS = "tags";
    private static final String CREATE_DATE = "create_date";
    private static final String SYMBOL = "%";
    private static final String PARAMETER_TAG_NAME_LIST = "tagNames";
    private static final String SELECT_TAG_BY_NAME = "FROM Tag t WHERE t.name IN (:" + PARAMETER_TAG_NAME_LIST + ")";

    public CriteriaQuery<GiftCertificate> buildQuery(EntityManager manager, GiftCertificateQueryParameters parameters) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        List<Predicate> predicates = new ArrayList<>();

        checkName(builder, root, predicates, parameters.getName());
        checkDescription(builder, root, predicates, parameters.getDescription());
        checkTags(manager, builder, root, predicates, parameters.getTagNames());

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        String sortColumn = checkSortColumn(parameters.getSortType());
        checkOrderType(sortColumn, parameters.getOrderType(), criteriaQuery, builder, root);

        return criteriaQuery;

    }

    private void checkOrderType(String sortColumn, OrderType orderType, CriteriaQuery<GiftCertificate> criteriaQuery, CriteriaBuilder builder, Root<GiftCertificate> root) {
        if (Objects.nonNull(orderType)) {
            Order order = null;
            if(Objects.isNull(sortColumn)) {
                sortColumn = NAME;
            }

            switch (orderType) {
                case ASC:
                    order = builder.asc(root.get(sortColumn));
                    break;
                case DESC:
                    order = builder.desc(root.get(sortColumn));
                    break;
                default:
                    throw new GiftCertificateQueryException();
            }

            criteriaQuery.orderBy(order);
        }
    }

    private String checkSortColumn(SortType sortType) {
        if(Objects.nonNull(sortType)) {
            switch (sortType) {
                case DATE:
                    return CREATE_DATE;
                case NAME:
                    return NAME;
                default:
                    throw new GiftCertificateQueryException();
            }
        }
        return null;
    }

    private void checkTags(EntityManager manager, CriteriaBuilder builder, Root<GiftCertificate> root, List<Predicate> predicates, Set<String> tagNames) {
        if (tagNames != null) {
            Set<Tag> tags = new HashSet<>(manager.createQuery(SELECT_TAG_BY_NAME, Tag.class).setParameter(PARAMETER_TAG_NAME_LIST, tagNames).getResultList());
            tags.forEach(tag -> predicates.add(builder.isMember(tag, root.get(TAGS))));

        }
    }

    private void checkDescription(CriteriaBuilder builder, Root<GiftCertificate> root, List<Predicate> predicates, String description) {
        if (Objects.nonNull(description)) {
            predicates.add(builder.like(root.get(DESCRIPTION), SYMBOL + description + SYMBOL));
        }
    }

    private void checkName(CriteriaBuilder builder, Root<GiftCertificate> root, List<Predicate> predicates, String name) {
        if (Objects.nonNull(name)) {
            predicates.add(builder.like(root.get(NAME), SYMBOL + name + SYMBOL));
        }
    }

}
