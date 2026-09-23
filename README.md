# Expense Tracker

A Java console application for tracking personal spending.

## Features
- Add expenses with an amount, category, description and date
- View all expenses and delete ones you no longer want
- Spending summary showing the total per category, percentage share and average expense
- Expenses are saved to a file (expenses.csv) and reloaded when the program starts
- Input validation for amounts, menu choices and dates

## Concepts used
Object-oriented programming, ArrayList, TreeMap, file input/output, exception handling, date parsing.

## Run
Requires Java (JDK) installed.

Windows: double-click run.bat
Mac/Linux: ./run.sh

Or manually:

    javac Expense.java ExpenseTracker.java
    java ExpenseTracker
