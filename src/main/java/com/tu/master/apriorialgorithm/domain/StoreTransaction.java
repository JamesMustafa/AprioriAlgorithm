package com.tu.master.apriorialgorithm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class StoreTransaction {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "customer_id")
    private Integer customerId;

    private Date date;

    private String items;

    public List<String> getItems() {
        return Arrays.asList(items.split(","));
    }

}
