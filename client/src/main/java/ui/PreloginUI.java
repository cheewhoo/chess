package ui;


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
            case 2 -> handleRegister();
            case 3 -> {
                return handleLogin(); // Return true if login is successful
            }
            case 4 -> quit();
            default -> System.out.println("Invalid choice. Try again.");
        }
        return false;
    }

    private int getUserChoice() {
        System.out.print("Choose an option: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private void displayHelp() {
        System.out.println("Help Menu:");
        System.out.println(" - Register: Register a new account.");
        System.out.println(" - Login: Log in to your account.");
        System.out.println(" - Quit: Exit the program.");
    }

    private void handleRegister() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        boolean success = serverFacade.register(username, password, email);
        if (success) {
            System.out.println("Registration successful. Please login.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private boolean handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean success = serverFacade.login(username, password);
        if (success) {
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
        return success;
    }

    private void quit() {
        System.out.println("Exiting Chess Client. Goodbye!");
        System.exit(0);
    }
}
