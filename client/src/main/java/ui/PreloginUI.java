package ui;


import java.util.Map;
import java.util.Scanner;

public class PreloginUI {
    private final ServerFacade serverFacade;
    private final Scanner scanner;

    public PreloginUI(ServerFacade serverFacade, Scanner scanner) {
        this.serverFacade = serverFacade;
        this.scanner = scanner;
    }

    public boolean showMenu() {
        System.out.println("\n-- Prelogin Menu --");
        System.out.println("1. Help");
        System.out.println("2. Register");
        System.out.println("3. Login");
        System.out.println("4. Quit");

        int choice = getUserChoice();

        switch (choice) {
            case 1 -> displayHelp();
            case 2 -> { return handleRegister();}
            case 3 -> {
                return handleLogin();
            }
            case 4 -> quit();
            default -> System.out.println("Invalid choice. Try again.");
        }
        return false;
    }

    private int getUserChoice() {
        int choice;
        while (true) {
            System.out.print("Choose an option: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 4) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please select a number between 1 and 4.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
        return choice;
    }

    private void displayHelp() {
        System.out.println("Help Menu:");
        System.out.println(" - Register: Register a new account.");
        System.out.println(" - Login: Log in to your account.");
        System.out.println(" - Quit: Exit the program.");
    }

    private boolean handleRegister() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        Map<String, Object> response = serverFacade.register(username, password, email);
        if (response.containsKey("success") && (boolean) response.get("success")) {
            System.out.println("Registration successful. Please login.");
            return true;
        } else {
            System.out.println("Registration failed: " + response.get("error"));
            return false;
        }
    }

    private boolean handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Map<String, Object> response = serverFacade.login(username, password);
        if (response.containsKey("success") && (boolean) response.get("success")) {
            System.out.println("Login successful. Welcome, " + username + "!");
            return true;
        } else {
            System.out.println("Login failed: " + response.get("error"));
            return false;
        }
    }

    private void quit() {
        System.out.println("Exiting Chess Client. Goodbye!");
        System.exit(0);
    }
}
