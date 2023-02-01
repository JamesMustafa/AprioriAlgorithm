package com.tu.master.apriorialgorithm;

import ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94;
import ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AssocRules;
import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AprioriService {

    private static final Double MINIMAL_SUPPORT = 0.002;
    private static final Double MINIMAL_CONFIDENCE = 0.01;
    public static final String APRIORI_OUTPUT_FILE = "frequent-items.txt";
    public static final String ASSOC_RULES_OUTPUT_FILE = "association-rules.txt";

    public void execute(Double support, Double confidence) throws IOException {
        AlgoApriori apriori = new AlgoApriori();

        if (support == null) {
            support = MINIMAL_SUPPORT;
        }

        if (confidence == null) {
            confidence = MINIMAL_CONFIDENCE;
        }
        else {
            confidence = confidence / 100;
        }

        apriori.runAlgorithm(support, TransactionService.TRANSACTION_FILE_NAME, APRIORI_OUTPUT_FILE);
        AlgoAgrawalFaster94 associationRules = new AlgoAgrawalFaster94();
        Itemsets frequentItemsets = parseResults();
        associationRules.runAlgorithm(frequentItemsets, ASSOC_RULES_OUTPUT_FILE, apriori.getDatabaseSize(), confidence);
    }

    private Itemsets parseResults() {
        List<String> results = FileParser.readFile(APRIORI_OUTPUT_FILE);
        Itemsets itemsets = new Itemsets("frequent-item-sets");
        results.forEach(line -> {
            int endIndex = line.indexOf('#');
            int support = Integer.parseInt(line.substring(line.indexOf(":")).replace(":", "").strip());

            List<Integer> resultItems = Arrays.stream(line.substring(0, endIndex).split(" "))
                    .map(Integer::parseInt).collect(Collectors.toList());

            itemsets.addItemset(new Itemset(resultItems, support), resultItems.size());


        });
        return itemsets;
    }

}
