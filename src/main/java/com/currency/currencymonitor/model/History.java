package com.currency.currencymonitor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "history")
public class History {
    @JsonIgnore
    @ToString.Exclude
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ToString.Exclude
    @Getter @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @NonNull
    @Getter @Setter
    @Column(name = "price")
    private Double price;

    @NonNull
    @Getter @Setter
    @Column(name = "timestamp")
    private long timestamp;
}
