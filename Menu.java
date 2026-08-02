import java.io.*;
import java.util.*;

// Features:
// 1. loadMenu() - Reads menu items from file
// 2. displayMenu() - Shows available items
// 3. updateItem() - Changes item details (used by Manager)

public class Menu {
    private ArrayList<MenuItem> items = new ArrayList<>();

    public void loadMenu(String filename) {
        items.clear(); // Clear existing items before loading
        try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length == 2) {
                    items.add(new MenuItem(parts[0], Double.parseDouble(parts[1])));
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load menu.");
        }
    }

    public void displayMenu() {
        System.out.println("\n--- CURRENT MENU ---");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }
    }

    public MenuItem getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public void updateItem(int index, String name, double price) {
        if (index >= 0 && index < items.size()) {
            items.get(index).setName(name);
            items.get(index).setPrice(price);
        }
    }

    public int size() {
        return items.size();
    }
}

class MenuItem {
    private String name;
    private double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return name + " - RM" + String.format("%.2f", price);
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
}
