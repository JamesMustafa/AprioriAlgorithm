package com.tu.master.apriorialgorithm.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "transactions")
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
