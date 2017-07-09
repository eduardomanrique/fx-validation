package com.eduardomanrique.fxvalidation.entity;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity()
@Immutable
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ACTIVE")
    private Boolean active;

    public Customer() {
    }

    public Customer(String name) {
        this.setName(name);
    }
}
