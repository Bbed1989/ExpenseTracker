package org.example;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        Map<String, Object> expenseMap = new HashMap<>();
        expenseMap.put("id", expense.getId());
        expenseMap.put("description", expense.getDescription());
        expenseMap.put("date", expense.getDate());
        expenseMap.put("amount", expense.getAmount());

         expenses.add(expenseMap);

        writeExpenses(expenses);
    }

    public boolean updateExpense(int id,  String description, int amount) throws IOException {
        List<Map<String, Object>> expenses = readExpenses();
        boolean isUpdated = false;
        for (Map<String, Object> expenseMap : expenses) {
            if ((int) expenseMap.get("id") == id) {
                expenseMap.put("description", description);
                expenseMap.put("amount", amount);
                isUpdated = true;
                break;
            }
        }
        if(isUpdated) {
            writeExpenses(expenses);
        }
        return isUpdated;
    }

    private void writeExpenses(List<Map<String, Object>> expenses) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            // filter empty maps
            List<Map<String, Object>> nonEmptyExpenses = expenses.stream()
                    .filter(expense -> !expense.isEmpty())  //skip empty
                    .toList();

            if (nonEmptyExpenses.isEmpty()) {
                writer.write("[]");
            } else {
                writer.write("[\n");
                String jsonContent = nonEmptyExpenses.stream()
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

    private List<Map<String, Object>> readExpenses() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        String jsonString = readFileContent(file).trim();

        if (jsonString.isEmpty() || jsonString.equals(EMPTY_JSON)) {
            return new ArrayList<>();
        }
        return parseExpensesFromJson(jsonString);
    }

    // Helper method to read the content of a file
    private String readFileContent(File file) throws IOException {
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        }
        return json.toString();
    }

    // Helper method to parse a JSON string into a List of Maps
    private List<Map<String, Object>> parseExpensesFromJson(String jsonString) {
        List<Map<String, Object>> expenses = new ArrayList<>();
        String[] objects = jsonString.substring(1, jsonString.length() - 1).split("},\\{");
        for (String obj : objects) {
            obj = obj.trim();
            if (!obj.startsWith("{")) obj = "{" + obj;
            if (!obj.endsWith("}")) obj = obj + "}";
            expenses.add(parseJsonToMap(obj));
        }
        return expenses;
    }

    private Map<String, Object> parseJsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        String[] pairs = json.substring(1, json.length() - 1).split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            String key = keyValue[0].trim().replaceAll("\"", "");
            String value = keyValue[1].trim().replaceAll("\"", "");
            map.put(key, value);
        }
        return map;
    }
}
