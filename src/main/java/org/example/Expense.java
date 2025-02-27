package org.example;

import java.time.LocalDate;

public class Expense {
    private final int id;
    private String description;
    private final LocalDate date;
    private int amount;


    public Expense(int id, String description, LocalDate date, int amount) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}
