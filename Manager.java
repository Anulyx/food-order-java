import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Manager extends User {
    public Manager(String username, String password) {
        super(username, password);
    }

    public void updateMenu(Menu menu, int index, String newName, double newPrice) {
        // Update in memory
        menu.updateItem(index, newName, newPrice);
        
        // Save to file
        saveMenuToFile(menu, "menu.txt");
        
        System.out.println("Menu updated successfully.");
    }

    private void saveMenuToFile(Menu menu, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                pw.println(item.getName() + "," + item.getPrice());
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save menu updates to file.");
        }
    }

    public void viewOrders(Customer customer) {
        customer.viewOrders();
    }
}