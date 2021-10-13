package com.epam.esm.entity;

import org.springframework.hateoas.RepresentationModel;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

@Entity(name = "Tag")
@Table(name = "tag")
public class Tag extends RepresentationModel<Tag> implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tag_tag",
            joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"))
    private Set<GiftCertificate> tag;

    public Set<GiftCertificate> getTag() {
        return tag;
    }

    public void setTag(Set<GiftCertificate> tag) {
        this.tag = tag;
    }

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag (String name) {
        this.name = name;
    }

    public Tag() { }

    @Override
    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
