package ui;

import java.util.Scanner;

public class PostloginUI {
    private final ServerFacade serverFacade;
    private final Scanner scanner;

    public PostloginUI(ServerFacade serverFacade, Scanner scanner) {
        this.serverFacade = serverFacade;
        this.scanner = scanner;
    }

    public boolean showMenu() {
        System.out.println("\n-- Postlogin Menu --");
        System.out.println("1. Help");
        System.out.println("2. Logout");
        System.out.println("3. Create Game");
        System.out.println("4. List Games");
        System.out.println("5. Quit");

        int choice = getUserChoice();

        switch (choice) {
            case 1 -> displayHelp();
            case 2 -> {
                return !handleLogout(); // Return false to transition to Prelogin if logged out
            }
            case 3 -> handleCreateGame();
            case 4 -> handleListGames();
            case 5 -> quit();
            default -> System.out.println("Invalid choice. Try again.");
        }
        return true;
    }

    private int getUserChoice() {
        System.out.print("Choose an option: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private void displayHelp() {
        System.out.println("Help Menu:");
        System.out.println(" - Logout: Log out of your account.");
        System.out.println(" - Create Game: Start a new chess game.");
        System.out.println(" - List Games: View available games.");
        System.out.println(" - Quit: Exit the program.");
    }

    private boolean handleLogout() {
        if (serverFacade.logout()) {
            System.out.println("Logged out successfully.");
            return true;
        } else {
            System.out.println("Logout failed.");
            return false;
        }
    }

    private void handleCreateGame() {
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine();

        int gameID = serverFacade.createGame(gameName);
        if (gameID != -1) {
            System.out.println("Game created successfully with ID: " + gameID);
        } else {
            System.out.println("Failed to create game.");
        }
    }

    private void handleListGames() {
        System.out.println("Listing games...");
        var games = serverFacade.listGames();
        if (games.isEmpty()) {
            System.out.println("No games found.");
        } else {
            int index = 1;
            for (var game : games) {
                System.out.println(index++ + ". " + game);
            }
        }
    }

    private void quit() {
        System.out.println("Exiting Chess Client. Goodbye!");
        System.exit(0);
    }
}
