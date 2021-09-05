package com.epam.esm.entity;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Tag implements Identifiable {

    private long id;
    private String name;

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
