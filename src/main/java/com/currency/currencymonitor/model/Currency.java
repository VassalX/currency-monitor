package com.currency.currencymonitor.model;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Entity
@Table(name = "currency")
public class Currency {
    @ToString.Exclude
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Getter @Setter
    @Column(name = "name_short")
    private String name_short;

    @Getter @Setter
    @Column(name = "name")
    private String name;

    @Getter @Setter
    @Column(name = "country")
    private String country;

    @Getter @Setter
    @Column(name = "growth")
    private Double growth;

    @Getter @Setter
    @Column(name = "price")
    private Double price;

    @Getter @Setter
    @Column(name = "timestamp")
    private long timestamp;

    @Getter @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "currency")
    private List<History> histories = new ArrayList<>();
}
