Expense Tracker

A simple command-line expense tracker application written in Java. This tool allows users to add, delete, list, and filter expenses while also supporting data export to a CSV file.

Features

Add new expenses with description, amount, and date.

List all expenses in a formatted table.

Filter expenses by month.

Calculate total expenses for a given month.

Export expenses to a CSV file.

Budget checking to alert when expenses exceed a limit.

Installation

Prerequisites

Java 17 or higher

Maven (optional, for dependency management)

Git (optional, for cloning the repository)

Clone the Repository

git clone https://github.com/Bbed1989/ExpenseTracker.git
cd ExpenseTracker

Build the Project

mvn clean install

Usage

Run the application using the command line:

java -jar target/ExpenseTracker.jar <command> [arguments]

Available Commands

Add an Expense

expense-tracker add "Lunch" 20

Adds an expense with description "Lunch" and amount $20.

List Expenses

expense-tracker list

Displays all recorded expenses.

Filter Expenses by Month

expense-tracker filter 04

Shows expenses from April, ignoring the year.

Get Total Expense for a Month

expense-tracker total 04

Calculates and displays the total expenses for April.

Export Expenses to CSV

expense-tracker export expenses.csv

Exports all expenses to a file named expenses.csv.

Check Budget

expense-tracker budget 500

Warns if total expenses exceed $500.

JSON Expense Format

Expenses are stored in a JSON file in the following format:

[
  {"id": "293", "date": "2025-04-14", "description": "gas", "amount": "10"},
  {"id": "294", "date": "2025-04-15", "description": "food", "amount": "15"}
]

Future Enhancements

Implement a GUI for better user experience.

Support multiple currencies.

Add recurring expense tracking.

License

This project is licensed under the MIT License.

https://roadmap.sh/projects/expense-tracker
