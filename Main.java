import java.util.*;
import java.io.*;
import javafx.scene.Parent;

//Handles:
//1. User registration/login
//2. Redirects to role-specific menus (Customer/Manager/Delivery)
//3. Manages file operations for user data (users.txt)

public class Main {
    private static final String USERS_FILE = "users.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        menu.loadMenu("menu.txt");

        System.out.println("=== Welcome to Online Food Order System ===");

        String username, password, role = null;

        // REGISTER or LOGIN
        System.out.print("Do you want to [1] Login or [2] Register? ");
        String choice = sc.nextLine();

        if (choice.equals("2")) {
            System.out.print("Choose role (customer / manager / delivery): ");
            role = sc.nextLine().toLowerCase();

            System.out.print("Enter new username: ");
            username = sc.nextLine();
            System.out.print("Enter password: ");
            password = sc.nextLine();

            if (register(username, password, role)) {
                System.out.println("✅ Registration successful! Please login.");
            } else {
                System.out.println("❌ Username already exists.");
                return;
            }
        }

        // LOGIN
        System.out.print("Enter username: ");
        username = sc.nextLine();
        System.out.print("Enter password: ");
        password = sc.nextLine();

        role = login(username, password);

        if (role == null) {
            System.out.println("❌ Login failed. Invalid credentials.");
            return;
        }

        System.out.println("✅ Logged in as " + role);

        // Load correct interface
        switch (role) {
            case "customer":
                Customer customer = new Customer(username, password);
                customer.login();
                handleCustomer(sc, customer, menu);
                customer.logout();
                break;

            case "manager":
                Manager manager = new Manager(username, password);
                manager.login();
                handleManager(sc, manager, menu);
                manager.logout();
                break;

            case "delivery":
                DeliveryPerson dp = new DeliveryPerson(username, password);
                dp.login();
                handleDelivery(sc, dp);
                dp.logout();
                break;

            default:
                System.out.println("❌ Invalid role.");
        }
    }

    // ======== LOGIN AND REGISTER METHODS ========
    public static String login(String username, String password) {
        try (Scanner sc = new Scanner(new File(USERS_FILE))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length == 3 && parts[0].equals(username) && parts[1].equals(password)) {
                    return parts[2]; // return role
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Could not read user file.");
        }
        return null;
    }

    public static boolean register(String username, String password, String role) {
        if (usernameExists(username)) return false;

        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE, true))) {
            pw.println(username + "," + password + "," + role.toLowerCase());
            return true;
        } catch (IOException e) {
            System.out.println("❌ Could not save new user.");
        }
        return false;
    }

    public static boolean usernameExists(String username) {
        try (Scanner sc = new Scanner(new File(USERS_FILE))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts[0].equals(username)) return true;
            }
        } catch (IOException e) {}
        return false;
    }

    // ============ CUSTOMER MENU ============
    public static void handleCustomer(Scanner sc, Customer customer, Menu menu) {
        String choice;
        do {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. Browse Menu");
            System.out.println("2. Place Order");
            System.out.println("3. View Orders");
            System.out.println("4. Make Payment");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = sc.nextLine();

            switch (choice) {
                case "1":
                    menu.displayMenu();
                    break;
                case "2":
                    customer.placeOrder(menu);
                    break;
                case "3":
                    customer.viewOrders();
                    break;
                // In the customer menu section:
                case "4":
                if (!customer.getOrders().isEmpty()) {
                    customer.getOrders().get(0).makePayment(); // Or handle multiple orders
                } else {
                    System.out.println("No orders to pay for.");
                }
                break;
                   
                default:
                    System.out.println("❌ Invalid choice.");
            }
        } while (!choice.equals("0"));
    }

    // ============ MANAGER MENU ============
    public static void handleManager(Scanner sc, Manager manager, Menu menu) {
        String choice;
        do {
            System.out.println("\n--- MANAGER MENU ---");
            System.out.println("1. Update Menu Item");
            System.out.println("2. View Customer Orders");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            choice = sc.nextLine();

            switch (choice) {
                // In the manager menu section:
            case "1":
                menu.displayMenu();
                System.out.print("Enter item number to update: ");
                int index = Integer.parseInt(sc.nextLine()) - 1;
    
                // Validate index
            if (index < 0 || index >= menu.size()) {
                System.out.println("❌ Invalid item number.");
                break;
            }
    
                System.out.print("Enter new name: ");
                String newName = sc.nextLine();
                System.out.print("Enter new price: ");
                double newPrice;
            try {
                newPrice = Double.parseDouble(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid price format.");
                    break;
            }
    
            manager.updateMenu(menu, index, newName, newPrice);
            break;
            }
        } while (!choice.equals("0"));
    }

    // ============ DELIVERY MENU ============
    public static void handleDelivery(Scanner sc, DeliveryPerson dp) {
        dp.viewAndUpdateOrders();
    }
}
