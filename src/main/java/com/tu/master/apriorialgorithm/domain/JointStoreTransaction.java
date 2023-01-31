package com.tu.master.apriorialgorithm.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "joint_transactions")
public class JointStoreTransaction extends StoreTransaction {

    @Builder
    public JointStoreTransaction(UUID id, Integer customerId, Date date, String items) {
        super(id, customerId, date, items);
    }
}
