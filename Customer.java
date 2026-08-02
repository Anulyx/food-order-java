import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

// Features:
// 1. placeOrder() - Adds items to cart from Menu
// 2. viewOrders() - Shows current orders
// 3. saveOrdersToFile() - Saves orders to orders.txt
// Uses Menu class to display food items

public class Customer extends User {
    private ArrayList<OrderDetails> orders;
    private Scanner scanner;

    public Customer(String username, String password) {
        super(username, password);
        orders = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void placeOrder(Menu menu) {
        menu.displayMenu();

        System.out.print("Enter item number to order: ");
        int itemIndex = scanner.nextInt() - 1;

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // clear newline

        if (itemIndex >= 0 && itemIndex < menu.size()) {
            OrderDetails order = new OrderDetails(menu.getItem(itemIndex), quantity);
            orders.add(order);
            System.out.println("✅ Order placed for " + quantity + " x " + menu.getItem(itemIndex).getName());
            saveOrdersToFile(); // 🔥 Save to file immediately
        } else {
            System.out.println("❌ Invalid item number.");
        }
    }

    public void viewOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders yet.");
        } else {
            for (OrderDetails order : orders) {
                order.displayOrder();
            }
        }
    }

    public ArrayList<OrderDetails> getOrders() {
        return orders;
    }

    public void saveOrdersToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("orders.txt", true))) {
            for (OrderDetails order : orders) {
                pw.println(order.getItem().getName() + "," +
                           order.getItem().getPrice() + "," +
                           order.getQuantity() + "," +
                           order.getStatus());
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save orders.");
        }
    }
}
