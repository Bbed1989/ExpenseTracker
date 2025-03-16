package org.example;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseService {
    private static final String FILE_NAME = "expenses.json";
    private static final String CSV_FILE = "expenses.csv";
    private static final String EMPTY_JSON = "[]";
    private final static int MAX_EXPENSES = 100;

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
        expenseMap.put("id", expense.id());
        expenseMap.put("description", expense.description());
        expenseMap.put("date", expense.date());
        expenseMap.put("amount", expense.amount());

        expenses.add(expenseMap);

        writeExpenses(expenses);
    }

    public boolean updateExpense(int id,  String description, int amount) throws IOException {
        List<Map<String, Object>> expenses = readExpenses();
        boolean isUpdated = false;
        for (Map<String, Object> expenseMap : expenses) {
            if (Integer.parseInt(expenseMap.get("id").toString()) == id) {
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


    public boolean deleteExpense(int id) throws IOException {
        List<Map<String, Object>> expenses = readExpenses();
        boolean isDeleted = false;
        for (Map<String, Object> expenseMap : expenses) {
            if (Integer.parseInt(expenseMap.get("id").toString()) == id) {
                expenses.remove(expenseMap);
                isDeleted = true;
                break;
            }
        }
        if(isDeleted) {
            writeExpenses(expenses);
        }
        return isDeleted;
    }
    
    public void getAllExpenses() throws IOException {
        List<Map<String, Object>> expenses = readExpenses();
        System.out.println("#  ID   Date       Description    Amount");
        System.out.println("# --------------------------------------");

        for (Map<String, Object> expense : expenses) {
            System.out.printf("#  %-4d %-10s %-12s $%-6d\n",
                    Integer.parseInt(expense.get("id").toString()),
                    expense.get("date"),
                    expense.get("description"),
                    Integer.parseInt(expense.get("amount").toString()));
        }
    }

    public void getSummary(String[] args) throws IOException {
        List<Map<String, Object>> expenses = readExpenses();
        if (args.length < 2) {
            int totalAmount = expenses.stream()
                    .mapToInt(expense -> Integer.parseInt(expense.get("amount").toString()))
                    .sum();

            System.out.printf("Total expenses: $%-6d\n", totalAmount);
        } else {
            int month = Integer.parseInt(args[1]);
            int totalAmount = expenses.stream()
                    .filter(expense -> {
                        String dateString = expense.get("date").toString();
                        LocalDate date = LocalDate.parse(dateString);
                        return date.getMonthValue() == month;
                    })
                    .mapToInt(expense -> Integer.parseInt((String) expense.get("amount")))
                    .sum();
            System.out.println("Total expenses for month " + getMonth(month) + ": $" + totalAmount);
        }
    }

    public void checkBudget() throws IOException {
        List<Map<String, Object>> expenses = readExpenses();
        int totalAmount = expenses.stream()
                .mapToInt(expense -> Integer.parseInt(expense.get("amount").toString()))
                .sum();
        if (totalAmount >= MAX_EXPENSES){
            System.out.println("Budget exceeded!");
        }
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

    public void exportToCSV() throws IOException {
        List<Map<String, Object>> expenses = readExpenses();
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            // Write header
            writer.append("ID,Date,Description,Amount\n");

            // Write each expense
            for (Map<String, Object> expense : expenses) {
                writer.append(expense.get("id").toString()).append(",")
                        .append(expense.get("date").toString()).append(",")
                        .append(expense.get("description").toString()).append(",")
                        .append(expense.get("amount").toString()).append("\n");
            }

            System.out.println("Expenses exported to " + CSV_FILE);
        }
    }

    private String getMonth(int month){
        return switch (month) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> "Invalid month";
        };
    }
}
