package com.currency.currencymonitor.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "currency")
public class Currency {
    @ToString.Exclude
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonProperty("name")
    @NonNull
    @Getter @Setter
    @Column(name = "short_name", unique=true)
    private String shortName;

    @JsonProperty("fullName")
    @NonNull
    @Getter @Setter
    @Column(name = "name")
    private String name;

    @NonNull
    @Getter @Setter
    @Column(name = "country")
    private String country;

    @NonNull
    @Getter @Setter
    @Column(name = "type")
    private String type;

    @NonNull
    @Getter @Setter
    @Column(name = "growth")
    private Double growth;

    @NonNull
    @Getter @Setter
    @Column(name = "price")
    private Double price;

    @JsonProperty("lastUpdateTimestamp")
    @NonNull
    @Getter @Setter
    @Column(name = "timestamp")
    private long timestamp;

    @ToString.Exclude
    @Getter @Setter
    @JsonIgnore
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "currency",
            cascade = CascadeType.ALL)
    private List<History> histories = new ArrayList<>();

    public void addToHistories(History history){
        histories.add(history);
        history.setCurrency(this);
    }
}
