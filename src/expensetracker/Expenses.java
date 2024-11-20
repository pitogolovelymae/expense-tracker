package expensetracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Expenses {
    Scanner scan = new Scanner(System.in);
    Config conf = new Config();

    public void expenseConfig(int id) {
        int option;
        do {
            System.out.println("\n--- Expenses Menu ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Edit Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Exit");
            try {
                System.out.print("\nChoose an option: ");
                option = scan.nextInt();
                scan.nextLine(); 

                switch (option) {
                    case 1:
                        addExpense(id);
                        break;
                    case 2:
                        viewExpenses(id); 
                        break;
                    case 3:
                        editExpense();
                        break;
                    case 4:
                        deleteExpense();
                        break;
                    case 5:
                        System.out.println("Exiting Expenses Menu.");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scan.nextLine(); 
                option = -1; 
            }
            
        } while (option != 5);
    }

    private void addExpense(int id) {
        System.out.println("Enter Expense Details:");
        
        System.out.print("\nCategory: ");
        String category = scan.nextLine();
        
        System.out.print("Amount: ");
        double amount = scan.nextDouble();
        scan.nextLine(); 
        
        String sql = "INSERT INTO expenses (user_id, category, amount, date) VALUES (?, ?, ?, ?)";       
        conf.addRecord(sql, id, category, amount, dateToday());
    }

    public void viewExpenses(int id) {
        String query = "SELECT id, category, amount, date FROM expenses WHERE user_id = " + id;
        String[] Headers = {"Expense ID", "Category", "Amount", "Date"};
        String[] Columns = {"id", "category", "amount", "date"};
        
        
        System.out.println("\nUsername : " + conf.getDataFromID("users", id, "username"));
        System.out.println("Expenses List");
        conf.viewRecords(query, Headers, Columns);
    }

    private void editExpense() {
        int ex_id;
        do {
            System.out.print("\nEnter Expense ID: ");
            ex_id = scan.nextInt();
            if (!conf.doesIDExist("expenses", ex_id)) {
                System.out.println("Expense ID Doesn't Exist.");
            }
        } while (!conf.doesIDExist("expenses", ex_id));
        scan.nextLine();
        
        System.out.println("\nEnter New Expense Details:");
        
        System.out.print("\nNew Category: ");
        String category = scan.nextLine();
        
        System.out.print("New Amount: ");
        double amount = scan.nextDouble();
        scan.nextLine();
        
        System.out.println("");
        String sql = "UPDATE expenses SET category = ?, amount = ?, date = ? WHERE id = ?";
        conf.updateRecord(sql, category, amount, dateToday(), ex_id);
    }

    private void deleteExpense() {
        System.out.print("Enter Expense ID you want to delete: ");
        int id = scan.nextInt();
        
        String sql = "DELETE FROM expenses WHERE id = ?";
        conf.deleteRecord(sql, id, true);
    }
    
    public String dateToday(){       
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd, yyyy");       
        return LocalDateTime.now().format(format);
    }
}
