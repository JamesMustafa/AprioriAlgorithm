package com.tu.master.apriorialgorithm;

import com.tu.master.apriorialgorithm.domain.StoreTransaction;
import com.tu.master.apriorialgorithm.domain.StoreTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final StoreTransactionRepository transactionRepository;

    public TransactionService(StoreTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    //TODO: Common are all transactions which are made by one customer in a single day
    public List<StoreTransaction> getCombinedTransactions() {
        List<StoreTransaction> transactions = new ArrayList<>(transactionRepository.findAll());
        List<StoreTransaction> detailedTransactions = new ArrayList<>();

        transactions.forEach(transaction -> {
            List<StoreTransaction> commonTransactions = filterCommonTransactions(transaction, transactions);
            StoreTransaction jointTransaction = getJointTransaction(commonTransactions);
            detailedTransactions.add(jointTransaction);
        });

        return detailedTransactions;
    }

    private List<StoreTransaction> filterCommonTransactions(StoreTransaction transaction,
                                                            List<StoreTransaction> allTransactions) {
        return allTransactions.stream()
                .filter(t -> t.getCustomerId().equals(transaction.getCustomerId()) &&
                        t.getDate().equals(transaction.getDate()))
                .collect(Collectors.toList());
    }

    private StoreTransaction getJointTransaction(List<StoreTransaction> commonTransactions) {
        StoreTransaction commonTransaction = commonTransactions.get(0);
        String items = joinTransactionItems(commonTransactions);

        return StoreTransaction.builder()
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