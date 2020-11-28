package com.currency.currencymonitor.model;

import lombok.*;
import javax.persistence.*;

@ToString
@Entity
@Table(name = "tutorials")
public class Currency {
    @ToString.Exclude
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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
}
