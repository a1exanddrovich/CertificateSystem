package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;
import com.epam.esm.auditTest.AuditUserListener;
import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(AuditUserListener.class)
public class User implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    public User(long id) {
        this.id = id;
    }

    public User() { }

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrePersist
    public void onPrePersist() {
        System.out.println("Persist");
    }

    @PreUpdate
    public void onPreUpdate() {
        System.out.println("Update");
    }

    @PreRemove
    public void onPreRemove() {
        System.out.println("Remove");
    }

}
