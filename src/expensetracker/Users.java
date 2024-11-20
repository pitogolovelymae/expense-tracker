package expensetracker;

import java.util.Scanner;

public class Users {
    Scanner scan = new Scanner(System.in);
    Config conf = new Config();

    public void userConfig(int id) {
        int option;
        do {
            viewUsers(id);
            
            System.out.println("\n--- Users Menu ---");
            System.out.println("1. Edit User");
            System.out.println("2. Delete User");
            System.out.println("3. Go back..");
            
            System.out.print("\nChoose an option: ");
            option = scan.nextInt();
            scan.nextLine(); 

            switch (option) {
                case 1:
                    editUser(id);
                    break;
                case 2:
                    String password;
                    
                    System.out.print("\nDelete User? Enter Password to Confirm: ");
                    password = scan.nextLine();

                    String username = conf.getDataFromID("users", id, "username");

                    if (conf.validateLogin(username, password)){
                        deleteUser(id);
                        
                    }else{
                        System.out.println("Wrong Password. Account Deletion Failed.");
                        return; 
                    }
                    
                case 3:
                    System.out.println("Going back..");
                    option = 3;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (option != 3);
    }

    public void addUser() {
        System.out.println("\nEnter User Details:");
        
        System.out.print("\nEmail: ");
        String email = scan.nextLine();
        
        System.out.print("Username: ");
        String username = scan.nextLine();
        
        System.out.print("Password: ");
        String password = scan.nextLine();
        
        System.out.println("");
        if (!conf.register(username)) {
            System.out.println("Registration failed. Username might already exist.");
            return;
        }
        
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";       
        conf.addRecord(sql, username, password, email);
    }

    public void viewUsers(int id) {
        String query = "SELECT * FROM users WHERE id = " + id;
        String[] Headers = {"User ID", "Username", "Email"};
        String[] Columns = {"id", "username", "email"};
        
        System.out.println("\n\t\t\t\t--- User Profile ---");
        conf.viewRecords(query, Headers, Columns);
    }

    private void editUser(int id) {
        
        System.out.println("\nEnter New User Details:");
        
        System.out.print("New Email: ");
        String email = scan.nextLine();
        
        System.out.print("New Username: ");
        String username = scan.nextLine();
        
        System.out.println("");
        String sql = "UPDATE users SET username = ?, email = ? WHERE id = ?";
        conf.updateRecord(sql, username, email, id);
    }

    private void deleteUser(int id) {
        
        String sql = "DELETE FROM users WHERE id = ?";
        conf.deleteRecord(sql, id, true);
        
        String expSql = "DELETE FROM expenses WHERE user_id = ?";
        conf.deleteRecord(expSql, id, false);
    }

    
}
