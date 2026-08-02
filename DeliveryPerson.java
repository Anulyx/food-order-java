import java.util.*;
import java.io.*;

// Features:
// 1. viewAndUpdateOrders() - Lists pending orders
// 2. updateOrderStatus() - Marks orders as "Delivered"
// Reads/Writes to orders.txt

public class DeliveryPerson extends User {
    public DeliveryPerson(String username, String password) {
        super(username, password);
    }

    public void viewAndUpdateOrders() {
        ArrayList<OrderDetails> orders = new ArrayList<>();

        // Step 1: Read orders from file
        try (Scanner sc = new Scanner(new File("orders.txt"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                MenuItem item = new MenuItem(parts[0], Double.parseDouble(parts[1]));
                int qty = Integer.parseInt(parts[2]);
                String status = parts[3];

                OrderDetails order = new OrderDetails(item, qty);
                order.setStatus(status);
                orders.add(order);
            }
        } catch (IOException e) {
            System.out.println("❌ Could not load orders.");
            return;
        }

        if (orders.isEmpty()) {
            System.out.println("📭 No orders to display.");
            return;
        }

        for (int i = 0; i < orders.size(); i++) {
            System.out.print((i + 1) + ". ");
            orders.get(i).displayOrder();
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter order number to mark as Delivered: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;

        if (index >= 0 && index < orders.size()) {
            updateOrderStatus(orders.get(index), "Delivered");
            System.out.println("✅ Order marked as Delivered.");
            orders.remove(index);

            // Rewrite file without the delivered order
            try (PrintWriter pw = new PrintWriter(new FileWriter("orders.txt"))) {
                for (OrderDetails order : orders) {
                    pw.println(order.getItem().getName() + "," +
                               order.getItem().getPrice() + "," +
                               order.getQuantity() + "," +
                               order.getStatus());
                }
            } catch (IOException e) {
                System.out.println("❌ Failed to update orders file.");
            }
        } else {
            System.out.println("❌ Invalid order number.");
        }
    }

    public void updateOrderStatus(OrderDetails order, String status) {
        order.setStatus(status);
    }
}
