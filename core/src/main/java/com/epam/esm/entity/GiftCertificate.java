package com.epam.esm.entity;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class GiftCertificate implements Identifiable {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Duration duration;
    private ZonedDateTime creationDate;
    private ZonedDateTime lastUpdateDate;
    private Set<Tag> tags = new HashSet<>();

    public GiftCertificate(long id, String name, String description, BigDecimal price, Duration duration, ZonedDateTime creationDate, ZonedDateTime lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public GiftCertificate() { }

    @Override
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

    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    public ZonedDateTime getLastUpdateDate() {
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

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GiftCertificate that = (GiftCertificate) o;
        return Objects.equals(id, that.id)
                && name.equals(that.name)
                && description.equals(that.description)
                && price.equals(that.price)
                && duration.equals(that.duration)
                && creationDate.equals(that.creationDate)
                && lastUpdateDate.equals(that.lastUpdateDate)
                && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, creationDate, lastUpdateDate, tags);
    }

}
