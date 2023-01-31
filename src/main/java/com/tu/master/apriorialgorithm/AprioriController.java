package com.tu.master.apriorialgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apriori")
public class AprioriController {

    private static final Logger log = LoggerFactory.getLogger(AprioriController.class);

    private final AprioriService aprioriService;
    private final TransactionService transactionService;

    public AprioriController(AprioriService aprioriService, TransactionService transactionService) {
        this.aprioriService = aprioriService;
        this.transactionService = transactionService;
    }

    @GetMapping("/execute")
    public String executeApriori(
            @RequestParam(name = "process", required = false, defaultValue = "true") Boolean process,
            @RequestParam(name = "support", required = false) Double support) throws IOException {
        log.info("Execution request has been made. Support: {}, Process: {}", support, process);

        List<String> transactionItems = processTransactions(process);
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
        log.info("Transaction result is returned to user");
        return stringBuilder.toString();
    }

    private List<String> processTransactions(Boolean process) {
        if (process) {
            transactionService.processJoinTransactions();
        }
        return transactionService.getTransactionItems();
    }
}
