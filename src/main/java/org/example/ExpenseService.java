package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseService {
    private static final String FILE_NAME = "expenses.json";
    private static final String EMPTY_JSON = "[]";
    public Expense createExpense(String description, int amount) {
        return new Expense(
                (int) (Math.random() * 1000), // generating random Id
                description,
                LocalDate.now(),
                amount
        );
    }

    public void addExpense(Expense expense) throws IOException {
        List<Map<String, Object>> expenses = readExpenses();

        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("id", expense.getId());
        taskMap.put("description", expense.getDescription());
        taskMap.put("date", expense.getDate());
        taskMap.put("amount", expense.getAmount());

        expenses.add(taskMap);

        writeExpenses(expenses);
    }

    private void writeExpenses(List<Map<String, Object>> expenses) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            // filter empty maps
            List<Map<String, Object>> nonEmptyTasks = expenses.stream()
                    .filter(expense -> !expense.isEmpty())  //skip empty
                    .toList();

            if (nonEmptyTasks.isEmpty()) {
                writer.write("[]");
            } else {
                writer.write("[\n");
                String jsonContent = nonEmptyTasks.stream()
                        .map(this::mapToJson)
                        .collect(Collectors.joining(",\n"));
                writer.write(jsonContent);
                writer.write("\n]");
            }
        }
    }


    private String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");
            json.append("\"").append(entry.getValue()).append("\",");
        }
        if (json.length() > 1) {
            json.deleteCharAt(json.length() - 1); // Видалення останньої коми
        }
        json.append("}");
        return json.toString();
    }

    private List<Map<String, Object>> readExpenses() {
        return null;
    }
}
