package com.tu.master.apriorialgorithm;

import com.tu.master.apriorialgorithm.domain.StoreTransaction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class FileParser {

    public static void writeToSpmf(List<StoreTransaction> transactions, List<String> items, String filePath)
            throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        transactions.forEach(transaction -> {
            String itemIndexes = transaction.getItems().stream()
                    .map(item -> String.valueOf(items.indexOf(item)))
                    .collect(Collectors.joining(" "));

            printWriter.println(itemIndexes);
        });

        printWriter.close();
    }

    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
