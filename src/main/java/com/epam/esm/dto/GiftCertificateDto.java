package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private String creationDate;
    private String lastUpdateDate;
    private Set<String> tags = new HashSet<>();

    public GiftCertificateDto(long id, String name, String description, BigDecimal price, int duration, String creationDate, String lastUpdateDate, Set<String> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public GiftCertificateDto() { }

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

    public int getDuration() {
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

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public void addTag(String tag) {
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

        GiftCertificateDto that = (GiftCertificateDto) o;
        return Objects.equals(id, that.id)
                && name.equals(that.name)
                && description.equals(that.description)
                && price.equals(that.price)
                && duration == that.duration
                && creationDate.equals(that.creationDate)
                && lastUpdateDate.equals(that.lastUpdateDate)
                && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, creationDate, lastUpdateDate, tags);
    }

}
