package org.example;

import java.util.Scanner;

public class CommandHandler {

    private final Scanner SCANNER = new Scanner(System.in);

    public void handleCommand(String[] args) {

        System.out.println("Handling command: " + args[0]);
    }
}
