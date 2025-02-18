package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Git Hub User Activity app started");

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.handleCommand(args);
    }
}