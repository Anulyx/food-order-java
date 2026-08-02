import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

//Common features:
// Stores username/password
// Handles login/registration
// Checks if username exists
// Provides logout/resetPassword methods

public class User {
    protected String username;
    protected String password;
    private static final String USERS_FILE = "users.txt";

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Authentication methods
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

    private static boolean usernameExists(String username) {
        try (Scanner sc = new Scanner(new File(USERS_FILE))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts[0].equals(username)) return true;
            }
        } catch (IOException e) {}
        return false;
    }

    // Original User methods
    public void login() {
        System.out.println(username + " logged in.");
    }

    public void logout() {
        System.out.println(username + " logged out.");
    }

    public void resetPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password reset successful.");
    }
}