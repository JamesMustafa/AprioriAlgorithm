package com.tu.master.apriorialgorithm;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AprioriService {

    private static final Double MINIMAL_SUPPORT = 0.2;

    //TODO: Stupid algorithm cannot work with strings :D / Should put items into map and set an integer id
    public static void main(String[] args) throws IOException {
        AlgoApriori apriori = new AlgoApriori();
        apriori.runAlgorithm(MINIMAL_SUPPORT, "sample_fpgrowth.txt", "result.txt");
        apriori.printStats();
    }
}
