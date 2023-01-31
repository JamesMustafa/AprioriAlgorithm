package com.tu.master.apriorialgorithm.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JointStoreTransactionRepository extends JpaRepository<JointStoreTransaction, UUID> {
}
