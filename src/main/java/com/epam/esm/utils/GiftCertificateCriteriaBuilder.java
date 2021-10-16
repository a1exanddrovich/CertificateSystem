package com.epam.esm.utils;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateQueryException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GiftCertificateCriteriaBuilder {

    private static final String SYMBOL = "%";
    private static final String PARAMETER_TAG_NAME_LIST = "tagNames";
//    private static final String SELECT_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name IN (:" + PARAMETER_TAG_NAME_LIST + ")";
    private static final String SELECT_TAG_BY_NAME = "FROM Tag t WHERE t.name IN (:" + PARAMETER_TAG_NAME_LIST + ")";

    public CriteriaQuery<GiftCertificate> buildQuery(EntityManager manager, GiftCertificateQueryParameters parameters) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        List<Predicate> predicates = new ArrayList<>();

        String name = parameters.getName();
        if (Objects.nonNull(name)) {
            predicates.add(builder.like(root.get("name"), SYMBOL + name + SYMBOL));
        }

        String description = parameters.getDescription();
        if (Objects.nonNull(description)) {
            predicates.add(builder.like(root.get("description"), SYMBOL + description + SYMBOL));
        }

        Set<String> tagNames = parameters.getTagNames();
        if (tagNames != null) {
            Set<Tag> tags = manager.createQuery(SELECT_TAG_BY_NAME, Tag.class).setParameter(PARAMETER_TAG_NAME_LIST, tagNames).getResultList().stream().collect(Collectors.toSet());
            tags.forEach(tag -> predicates.add(builder.isMember(tag, root.get("tags"))));

        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        SortType sortType = parameters.getSortType();
        OrderType orderType = parameters.getOrderType();

        String column = null;

        if(Objects.nonNull(sortType)) {
            switch (sortType) {
                case DATE:
                    column = "create_date";
                    break;
                case NAME:
                    column = "name";
                    break;
                default:
                    throw new GiftCertificateQueryException();
            }
        }

        if (Objects.nonNull(orderType)) {
            Order order = null;
            if(Objects.isNull(column)) {
                column = "name";
            }

            switch (orderType) {
                case ASC:
                    order = builder.asc(root.get(column));
                    break;
                case DESC:
                    order = builder.desc(root.get(column));
                    break;
                default:
                    throw new GiftCertificateQueryException();
            }

            criteriaQuery.orderBy(order);
        }

        return criteriaQuery;

    }

}
