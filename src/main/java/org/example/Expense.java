package org.example;

import java.time.LocalDate;

public record Expense(int id, String description, LocalDate date, int amount) {

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
