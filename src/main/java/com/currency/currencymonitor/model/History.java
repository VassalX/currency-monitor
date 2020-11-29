package com.currency.currencymonitor.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Entity
@Table(name = "history")
public class History {
    @ToString.Exclude
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Getter @Setter
    @Column(name = "price")
    private Double price;

    @Getter @Setter
    @Column(name = "timestamp")
    private long timestamp;
}
