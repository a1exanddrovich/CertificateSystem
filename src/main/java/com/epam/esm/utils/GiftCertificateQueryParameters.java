package com.epam.esm.utils;

import java.util.Objects;
import java.util.Set;

public class GiftCertificateQueryParameters {

    private String name;
    private String description;
    private Set<String> tagNames;
    private SortType sortType;
    private OrderType orderType;

    public GiftCertificateQueryParameters(String name, String description, Set<String> tagNames, String sortType, String orderType) {
        if (Objects.nonNull(name)) {
            this.setName(name);
        }

        if (Objects.nonNull(description)) {
            setDescription(description);
        }

        if (tagNames != null && !tagNames.isEmpty()) {
            this.tagNames = tagNames;
        }

        if (Objects.nonNull(sortType)) {
            setSortType(sortType.toUpperCase());
        }

        if (Objects.nonNull(orderType)) {
            setOrderType(orderType.toUpperCase());
        }

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getTagNames() {
        return tagNames;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTagNames(Set<String> tagNames) {
        this.tagNames = tagNames;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = SortType.valueOf(sortType.toUpperCase());
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = OrderType.valueOf(orderType.toUpperCase());
    }

    public boolean isEmpty() {
        return name == null && description == null && tagNames == null && sortType == null && orderType == null;
    }

}

