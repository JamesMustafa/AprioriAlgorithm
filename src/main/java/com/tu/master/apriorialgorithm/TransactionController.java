package com.tu.master.apriorialgorithm;

import com.tu.master.apriorialgorithm.domain.StoreTransaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService aprioriService;

    public TransactionController(TransactionService aprioriService) {
        this.aprioriService = aprioriService;
    }

    @GetMapping("/convert")
    public String convertItems() throws IOException {
        var transactions = aprioriService.getJointTransactions();
        aprioriService.convertTransactionsToSpmf(transactions);
        return "Success";
    }

    @GetMapping("/combine")
    public String combineTransactions() {
        aprioriService.processJoinTransactions();
        return "Success";
    }

}
