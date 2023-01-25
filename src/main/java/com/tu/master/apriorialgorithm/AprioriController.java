package com.tu.master.apriorialgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/apriori")
public class AprioriController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AprioriController.class);

    private final AprioriService aprioriService;
    private final TransactionService transactionService;

    public AprioriController(AprioriService aprioriService, TransactionService transactionService) {
        this.aprioriService = aprioriService;
        this.transactionService = transactionService;
    }

    @GetMapping("/execute")
    public String executeApriori(
            @RequestParam(name = "convert", required = false, defaultValue = "false") Boolean convert,
            @RequestParam(name = "support", required = false) Double support) throws IOException {
        LOGGER.info("Execution request has been made. Support: {}, Convert: {}", support, convert);

        List<String> transactionItems = processTransactions(convert);
        aprioriService.execute(support);

        return getResults(transactionItems);
    }

    private String getResults(List<String> transactionItems) {
        List<String> results = FileParser.readFile(AprioriService.APRIORI_OUTPUT_FILE);
        StringBuilder stringBuilder = new StringBuilder();
        results.forEach(line -> {
            int endIndex = line.indexOf('#');

            String resultItems = Arrays.stream(line.substring(0, endIndex).split(" "))
                    .map(index -> transactionItems.get(Integer.parseInt(index)))
                    .collect(Collectors.joining(", "));

            String endResult = line.substring(endIndex);
            stringBuilder.append(resultItems).append("  / ").append(endResult).append("<br>");
        });

        return stringBuilder.toString();
    }

    private List<String> processTransactions(Boolean convert) throws IOException {
        var transactions = transactionService.getCombinedTransactions();
        if (convert) {
            transactionService.convertTransactionsToSpmf(transactions);
        }
        return transactionService.getTransactionItems(transactions);
    }
}
