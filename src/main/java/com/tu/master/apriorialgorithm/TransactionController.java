package com.tu.master.apriorialgorithm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService aprioriService;

    public TransactionController(TransactionService aprioriService) {
        this.aprioriService = aprioriService;
    }

    @GetMapping("/size")
    public Integer getTransactionSize() {
        return aprioriService.getCombinedTransactions().size();
    }

}
