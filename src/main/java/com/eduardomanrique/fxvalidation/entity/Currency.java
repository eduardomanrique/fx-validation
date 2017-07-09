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
@Table(name = "CURRENCY")
public class Currency {

    @Id
    @Column(name = "ISO_CODE")
    private String isoCode;

    @Column(name = "NAME")
    private String name;

    public Currency() {
    }

    public Currency(String isoCode) {
        this.setIsoCode(isoCode);
    }
}
