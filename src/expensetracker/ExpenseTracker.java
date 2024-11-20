package expensetracker;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ExpenseTracker {
    static Config conf = new Config();
    static Scanner scan = new Scanner(System.in);
    static Users user = new Users();
    static Expenses expenses = new Expenses();
    
    public static void main(String[] args) {
        int isLoggedIn;
        int action = 0;

        do {
            try {
                System.out.println("\n   + Expense Management System +");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                
                System.out.print("\nEnter Option: ");
                action = scan.nextInt();
                scan.nextLine();

                switch (action) {
                    case 1:
                        isLoggedIn = login();
                        if (isLoggedIn > 0) {
                            mainMenu(isLoggedIn);
                        }
                        break;
                    case 2:
                        user.addUser();
                        break;
                    case 3:
                        System.out.println("Exiting system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid Option.");
                }
            } catch(InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scan.nextLine(); 
            }
        } while (action != 3);
    }

    private static void mainMenu(int id) {
        int choice;
        do {
            try {
                
                if(!conf.doesIDExist("users", id)){
                    return;
                }
                
                System.out.println("-------------------------------------------------------------------------------------");
                System.out.println("\nWelcome, " + conf.getDataFromID("users", id, "username"));
                System.out.println("User ID: " + id);
                
                System.out.println("\n--- Main Menu ---");
                System.out.println("1. Profile");
                System.out.println("2. Expenses");
                System.out.println("3. Logout");

                System.out.print("\nEnter Option: ");
                choice = scan.nextInt();
                scan.nextLine();
                
                System.out.println("-------------------------------------------------------------------------------------");

                switch (choice) {
                    case 1:
                        user.userConfig(id); 
                        break;
                    case 2:
                        expenses.expenseConfig(id); 
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        return;  
                    default:
                        System.out.println("Invalid Option.");
                }
                
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scan.nextLine(); 
                choice = -1; 
            }
        } while (true); 
    }
    
    private static int login() {
        System.out.print("\n>Enter Username: ");
        String username = scan.nextLine();
        System.out.print(">Enter Password: ");
        String password = scan.nextLine();
        
        if (conf.validateLogin(username, password)) {
            System.out.println("\nLogin successful!");
            return conf.getID("SELECT id FROM users WHERE username = ?", username);
        } else {
            System.out.println("Wrong Username or Password. Please try again.");
            return 0;
        }
    }

    static void generateReports() {
        System.out.println("\n\t\t\t--- USER EXPENSE REPORT ---");
        System.out.println("-------------------------------------------------------------------------------------");

//        user.viewUsers(); 

        int userId;
        do {
            System.out.print("\nEnter the User ID for the report: ");
            userId = scan.nextInt();
            if (!conf.doesIDExist("users", userId)) {
                System.out.println("Error: User ID not found. Please try again.");
            }
        } while (!conf.doesIDExist("users", userId));

        System.out.println("\n=====================================");
        System.out.println("           REPORT DETAILS            ");
        System.out.println("=====================================");

        System.out.printf("User ID        : %d%n", userId);
        System.out.printf("Username       : %s%n", conf.getDataFromID("users", userId, "username"));
        System.out.printf("Email Address  : %s%n", conf.getDataFromID("users", userId, "email"));
        System.out.println("-------------------------------------");

        if (conf.isTableEmpty("expenses WHERE user_id = " + userId)) {
            System.out.println("No expense records available for this user.");
            System.out.println("=====================================");
        } else {
            System.out.println("\nExpense Records: ");
            String sql = "SELECT id, category, amount, date " +
                         "FROM expenses " +
                         "WHERE user_id = " + userId;

            String[] Headers = {"Expense ID", "Category", "Amount", "Date"};
            String[] Columns = {"id", "category", "amount", "date"};

            conf.viewRecords(sql, Headers, Columns);
            System.out.println("\n=====================================");
        }
    }
}
