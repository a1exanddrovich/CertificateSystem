package com.epam.esm.entity;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class GiftCertificate implements Identifiable {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Duration duration;
    private String creationDate;
    private String lastUpdateDate;
    private Set<Tag> tags = new HashSet<>();

    public GiftCertificate(long id, String name, String description, BigDecimal price, Duration duration, String creationDate, String lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public GiftCertificate() { }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

}
