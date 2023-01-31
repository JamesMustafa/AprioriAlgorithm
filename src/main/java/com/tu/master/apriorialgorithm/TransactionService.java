package com.tu.master.apriorialgorithm;

import com.tu.master.apriorialgorithm.domain.JointStoreTransaction;
import com.tu.master.apriorialgorithm.domain.JointStoreTransactionRepository;
import com.tu.master.apriorialgorithm.domain.StoreTransaction;
import com.tu.master.apriorialgorithm.domain.StoreTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    public static final String TRANSACTION_FILE_NAME = "apriori-transactions.txt";
    private final StoreTransactionRepository transactionRepository;
    private final JointStoreTransactionRepository jointTransactionRepository;

    public TransactionService(StoreTransactionRepository transactionRepository, JointStoreTransactionRepository jointTransactionRepository) {
        this.transactionRepository = transactionRepository;
        this.jointTransactionRepository = jointTransactionRepository;
    }

    public List<JointStoreTransaction> getJointTransactions() {
        var jointTransactions = jointTransactionRepository.findAll();
        log.info("Fetched {} transactions for processing", jointTransactions.size());
        return jointTransactions;
    }

    public void processJoinTransactions() {
        log.info("Starting combining transactions");
        List<JointStoreTransaction> jointTransactions = combineTransactions();
        jointTransactionRepository.saveAll(jointTransactions);
        log.info("Combined transactions saved successfully");
    }

    public void convertTransactionsToSpmf(List<JointStoreTransaction> transactions) throws IOException {
        log.info("Starting to convert files into SPMF format");
        List<String> transactionItems = getTransactionItems();
        FileParser.writeToSpmf(transactions, transactionItems, TRANSACTION_FILE_NAME);
        log.info("Files are successfully persisted in SPMF format. File: {}", TRANSACTION_FILE_NAME);
    }

    //TODO: Common - made by one customer in a single day
    private List<JointStoreTransaction> combineTransactions() {
        List<StoreTransaction> transactions = new ArrayList<>(transactionRepository.findAll());
        List<JointStoreTransaction> detailedTransactions = new ArrayList<>();

        transactions.forEach(transaction -> {
            List<StoreTransaction> commonTransactions = filterCommonTransactions(transaction, transactions);
            JointStoreTransaction jointTransaction = buildJointTransaction(commonTransactions);
            detailedTransactions.add(jointTransaction);
        });

        return detailedTransactions;
    }

    public List<String> getTransactionItems() {
        return getJointTransactions().stream()
                .map(StoreTransaction::getItems)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    private List<StoreTransaction> filterCommonTransactions(StoreTransaction transaction,
                                                            List<StoreTransaction> allTransactions) {
        return allTransactions.stream()
                .filter(t -> t.getCustomerId().equals(transaction.getCustomerId()) &&
                        t.getDate().equals(transaction.getDate()))
                .collect(Collectors.toList());
    }

    private JointStoreTransaction buildJointTransaction(List<StoreTransaction> commonTransactions) {
        StoreTransaction commonTransaction = commonTransactions.get(0);
        String items = joinTransactionItems(commonTransactions);

        return JointStoreTransaction.builder()
                .id(UUID.randomUUID())
                .customerId(commonTransaction.getCustomerId())
                .date(commonTransaction.getDate())
                .items(items)
                .build();
    }

    private String joinTransactionItems(List<StoreTransaction> commonTransactions) {
        return commonTransactions.stream()
                .map(StoreTransaction::getItems)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.joining(", "));
    }

}
