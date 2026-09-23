import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ExpenseTracker {
    private static final String FILE_NAME = "expenses.csv";
    private static final String[] CATEGORIES = {"Food", "Transport", "Bills", "Entertainment", "Other"};

    private static ArrayList<Expense> expenses = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadExpenses();
        boolean running = true;

        while (running) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add expense");
            System.out.println("2. View all expenses");
            System.out.println("3. View spending summary");
            System.out.println("4. Delete an expense");
            System.out.println("5. Exit");

            int choice = readInt("Choose an option: ", 1, 5);

            if (choice == 1) {
                addExpense();
            } else if (choice == 2) {
                viewExpenses();
            } else if (choice == 3) {
                showSummary();
            } else if (choice == 4) {
                deleteExpense();
            } else {
                running = false;
                System.out.println("Goodbye!");
            }
        }
    }

    private static void addExpense() {
        double amount = readDouble("Amount (£): ", 0.01, 1000000);

        System.out.println("Categories:");
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println((i + 1) + ". " + CATEGORIES[i]);
        }
        int categoryChoice = readInt("Choose a category: ", 1, CATEGORIES.length);
        String category = CATEGORIES[categoryChoice - 1];

        System.out.print("Description: ");
        String description = scanner.nextLine().trim().replace(",", " ");
        if (description.isEmpty()) {
            description = "No description";
        }

        String date = readDate();

        expenses.add(new Expense(date, category, amount, description));
        saveExpenses();
        System.out.println("Expense added and saved.");
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }

    private static void showSummary() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }

        // Add up the total for each category
        Map<String, Double> totals = new TreeMap<>();
        double overall = 0;
        for (Expense e : expenses) {
            totals.put(e.getCategory(), totals.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
            overall += e.getAmount();
        }

        System.out.println("\n--- Spending Summary ---");
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            double percent = entry.getValue() / overall * 100;
            System.out.printf("%-13s £%8.2f (%.1f%%)%n", entry.getKey(), entry.getValue(), percent);
        }
        System.out.printf("%-13s £%8.2f%n", "TOTAL", overall);
        System.out.printf("Average expense: £%.2f%n", overall / expenses.size());
    }

    private static void deleteExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to delete.");
            return;
        }
        viewExpenses();
        int number = readInt("Number to delete: ", 1, expenses.size());
        expenses.remove(number - 1);
        saveExpenses();
        System.out.println("Expense deleted.");
    }

    // Saves every expense to a text file, one per line
    private static void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(FILE_NAME)) {
            for (Expense e : expenses) {
                writer.println(e.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Could not save expenses: " + e.getMessage());
        }
    }

    // Loads saved expenses when the program starts
    private static void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    expenses.add(Expense.fromFileString(line));
                } catch (Exception e) {
                    System.out.println("Skipped a bad line in the file.");
                }
            }
            System.out.println("Loaded " + expenses.size() + " saved expenses.");
        } catch (IOException e) {
            // No file yet, so start with an empty list
        }
    }

    // Asks for a date until it is valid, blank means today
    private static String readDate() {
        while (true) {
            System.out.print("Date (yyyy-mm-dd, or press Enter for today): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return LocalDate.now().toString();
            }
            try {
                return LocalDate.parse(input).toString();
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use the format 2026-09-23.");
            }
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number.");
            }
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number.");
            }
        }
    }
}
