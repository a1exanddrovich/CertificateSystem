package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;
import com.epam.esm.converter.LocalDateTimeConverter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "giftcertificate")
@EntityListeners(AuditListener.class)
public class GiftCertificate implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price", scale = 2, precision = 10)
    private BigDecimal price;
    @Column(name = "duration")
    private Duration duration;
    @Column(name = "create_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime creationDate;
    @Column(name = "last_update_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastUpdateDate;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "gift_certificate_tag",
               joinColumns = @JoinColumn(name = "gift_certificate_id"),
               inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public GiftCertificate(long id, String name, String description, BigDecimal price, Duration duration, LocalDateTime creationDate, LocalDateTime lastUpdateDate, Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public GiftCertificate() { }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getType() {
        return "Gift certificate";
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

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public LocalDateTime getLastUpdateDate() {
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

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
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
