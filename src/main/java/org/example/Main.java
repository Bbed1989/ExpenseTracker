package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Git Hub User Activity app started");

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.handleCommand(args);
    }
}