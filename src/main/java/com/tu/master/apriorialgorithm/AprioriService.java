package com.tu.master.apriorialgorithm;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AprioriService {

    private static final Double MINIMAL_SUPPORT = 0.02;
    public static final String APRIORI_OUTPUT_FILE = "result.txt";

    public void execute(Double support) throws IOException {
        AlgoApriori apriori = new AlgoApriori();

        if (support == null) {
            support = MINIMAL_SUPPORT;
        }

        apriori.runAlgorithm(support, TransactionService.TRANSACTION_FILE_NAME, APRIORI_OUTPUT_FILE);
        apriori.printStats();
    }

}
