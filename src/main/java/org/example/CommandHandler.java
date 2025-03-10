package org.example;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CommandHandler {
    private final ExpenseService expenseService = new ExpenseService();
    private final Scanner scanner = new Scanner(System.in);

    public void handleCommand(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            String input = scanner.nextLine().trim();
            args = input.split("\\s+", 2);
            System.out.println("Handling command: " + args[0]);
        }

        String command = args[0]; // first word is a command

        switch (command) {
            case "add":
                handleAdd(args);
                break;
            case "list":
                handleList(args);
                break;
            case "update":
                handleUpdate(args);
                break;
            case "delete":
                handleDelete(args);
                break;
            case "summary":
                handleStatus(args);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    private void handleStatus(String[] args) {
    }

    private void handleDelete(String[] args) {
    }

    private void handleUpdate(String[] args) throws IOException {
        int id = getId(args);
        String description = getDescription(args);
        int amount = getAmount(args);
        if (amount < 0) {
            throw new IOException("Amount cannot be negative");
        }
       boolean updated = expenseService.updateExpense(id, description, amount);
        if (updated) {
            System.out.println("Expense updated successfully: " + id);
        } else {
            System.out.println("No expense found with ID " + id);
        }
    }

    private void handleList(String[] args) {
    }

    private void handleAdd(String[] args) throws IOException {
        String description = getDescription(args);
        int amount = getAmount(args);
        if (amount < 0) {
            throw new IOException("Amount cannot be negative");
        }
        Expense expense = expenseService.createExpense(description, amount);
        expenseService.addExpense(expense);
        System.out.println("Expense added successfully: " + expense.getId());
    }

    private String getDescription(String[] args) {
        if (args.length < 2) {
            System.out.print("Enter new description: ");
            return scanner.nextLine().trim();
        } else return args[1];
    }

    private int getAmount(String[] args) {
        if (args.length < 4) {
            while (true) {
                System.out.print("Enter new amount: ");
                try {
                    return scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid amount. Please enter a number.");
                    scanner.nextLine();
                }
            }
        } else return Integer.parseInt(args[3]);
    }

    private int getId(String[] args) {
        if (args.length >= 2) {
            try {
                return Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid task ID. Please provide a numeric ID.");
                return -1;
            }
        }

        while (true) {
            System.out.print("Enter task ID: ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Please enter a numeric ID.");
            }
        }
    }
}

