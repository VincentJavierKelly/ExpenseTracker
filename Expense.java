// Represents a single expense
public class Expense {
    private String date;
    private String category;
    private double amount;
    private String description;

    public Expense(String date, String category, double amount, String description) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    // Converts the expense into one line of text so it can be saved to a file
    public String toFileString() {
        return date + "," + category + "," + amount + "," + description;
    }

    // Rebuilds an expense from a saved line of text
    public static Expense fromFileString(String line) {
        String[] parts = line.split(",", 4);
        return new Expense(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]);
    }

    public String toString() {
        return String.format("%s | %-13s | £%8.2f | %s", date, category, amount, description);
    }
}
