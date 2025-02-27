package org.example;

import java.util.Scanner;

public class CommandHandler {
    private final ExpenseService expenseService = new ExpenseService();
    private final Scanner scanner = new Scanner(System.in);

    public void handleCommand(String[] args) {

        System.out.println("Handling command: " + args[0]);

        if (args.length == 0) {
            System.out.println("Please enter a command.");
            String input = scanner.nextLine().trim();
            args = input.split("\\s+", 2);
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

    private void handleUpdate(String[] args) {
    }

    private void handleList(String[] args) {
    }

    private void handleAdd(String[] args) {
    }
}

