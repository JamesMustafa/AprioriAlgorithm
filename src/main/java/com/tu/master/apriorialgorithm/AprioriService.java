package com.tu.master.apriorialgorithm;

import com.tu.master.apriorialgorithm.domain.StoreTransaction;
import com.tu.master.apriorialgorithm.domain.StoreTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AprioriService {

    private static final Double MINIMAL_SUPPORT = 0.5;
    private final StoreTransactionRepository transactionRepository;

    public AprioriService(StoreTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public String convertDatabase() {
        List<String> idk = new ArrayList<>();
        List<StoreTransaction> transactions = new ArrayList<>(transactionRepository.findAll());
        transactions.forEach(transaction -> {
            var x = transactions.stream()
                    .filter(t -> t.getCustomerId().equals(transaction.getCustomerId()) &&
                            t.getDate().equals(transaction.getDate()))
                    .collect(Collectors.toList());
            StoreTransaction newTransaction = new StoreTransaction();
            newTransaction.setDate(transaction.getDate());
            newTransaction.setCustomerId(transaction.getCustomerId());

            String items = x.stream()
                    .map(StoreTransaction::getItems)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.joining(", "));

            newTransaction.setItems(items);
            System.out.println("New: " + newTransaction);
        });
        return "done";
    }

}
