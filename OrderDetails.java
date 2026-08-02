// Features:
// 1. calculateTotal() - Computes order cost
// 2. makePayment() - Handles payment logic (with discount)
// 3. setStatus() - Updates order status (e.g., "Pending" → "Delivered")


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderDetails {
    private MenuItem item;
    private int quantity;
    private String status;
    private static final Map<String, Double> DISCOUNT_COUPONS = new HashMap<>();
    
    static {
        DISCOUNT_COUPONS.put("HUNGRY10", 0.10);
        DISCOUNT_COUPONS.put("SAVE15", 0.15);
        DISCOUNT_COUPONS.put("BIG20", 0.20);
    }

    public OrderDetails(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
        this.status = "Pending";
    }

    // Calculate the total cost before any discounts
    public double calculateTotal() {
        return item.getPrice() * quantity;
    }

    public void makePayment() {
        Scanner scanner = new Scanner(System.in);
        double subtotal = calculateTotal(); // Now properly declared
        double discount = 0;
        String couponUsed = "None";
        
        // Apply RM5 discount for orders > RM50
        if (subtotal > 50) {
            discount += 5;
        }
        
        // Ask for coupon code
        System.out.print("\nDo you have a discount coupon? (Y/N): ");
        String hasCoupon = scanner.nextLine().trim().toUpperCase();
        
        if (hasCoupon.equals("Y")) {
            System.out.print("Enter coupon code: ");
            String couponCode = scanner.nextLine().trim().toUpperCase();
            
            if (DISCOUNT_COUPONS.containsKey(couponCode)) {
                double couponDiscount = subtotal * DISCOUNT_COUPONS.get(couponCode);
                discount += couponDiscount;
                couponUsed = couponCode + " (" + (DISCOUNT_COUPONS.get(couponCode)*100) + "%)";
            } else {
                System.out.println("Invalid coupon code. No additional discount applied.");
            }
        }
        
        double finalTotal = subtotal - discount;
        generateReceipt(subtotal, discount, couponUsed, finalTotal);
    }

    private void generateReceipt(double subtotal, double discount, String couponUsed, double finalTotal) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        
        String receipt = "\n=================================\n"
                       + "         FOOD ORDER RECEIPT        \n"
                       + "=================================\n"
                       + "Date: " + timestamp + "\n"
                       + "Order ID: " + generateOrderId() + "\n\n"
                       + "ITEM: " + item.getName() + "\n"
                       + "QTY: " + quantity + " x RM" + String.format("%.2f", item.getPrice()) + "\n"
                       + "---------------------------------\n"
                       + "Subtotal:      RM" + String.format("%.2f", subtotal) + "\n"
                       + "Discount:     -RM" + String.format("%.2f", discount) + "\n"
                       + "Coupon Used:   " + couponUsed + "\n"
                       + "---------------------------------\n"
                       + "TOTAL:         RM" + String.format("%.2f", finalTotal) + "\n"
                       + "=================================\n"
                       + "       THANK YOU FOR ORDERING!    \n"
                       + "=================================\n";
        
        System.out.println(receipt);
        saveReceiptToFile(receipt);
    }

    private String generateOrderId() {
        return "ORD" + System.currentTimeMillis() % 10000;
    }

    private void saveReceiptToFile(String receipt) {
        String filename = "receipt_" + generateOrderId() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println(receipt);
            System.out.println("Receipt saved to: " + filename);
        } catch (IOException e) {
            System.out.println("Could not save receipt to file.");
        }
    }

    // Other existing methods
    public void displayOrder() {
        System.out.println(quantity + " x " + item.getName() + " | Total: RM" + calculateTotal());
        System.out.println("Status: " + status);
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}