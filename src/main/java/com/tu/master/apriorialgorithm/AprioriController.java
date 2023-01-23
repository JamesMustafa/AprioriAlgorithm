package com.tu.master.apriorialgorithm;

import com.tu.master.apriorialgorithm.domain.StoreTransactionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class AprioriController {

    private final StoreTransactionRepository transactionRepository;
    private final AprioriService service;

    public AprioriController(StoreTransactionRepository transactionRepository, AprioriService service) {
        this.transactionRepository = transactionRepository;
        this.service = service;
    }

    @GetMapping
    public String getTransactions() {
        return service.convertDatabase();
    }

    @GetMapping("/{id}")
    public String getTransaction(@PathVariable String id) {
        var x = transactionRepository.findById(UUID.fromString(id)).orElseThrow();
        return x.getItems().toString();
    }
}
