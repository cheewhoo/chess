package ui;

import chess.ChessBoard;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PostloginUI {
    private final ServerFacade serverFacade;
    private final Scanner scanner;
    private List<Map<String, Object>> ListedGames;
    private ChessBoard chessBoard;
    private ChessBoardRenderer chessBoardRenderer;

    public PostloginUI(ServerFacade serverFacade, Scanner scanner) {
        this.serverFacade = serverFacade;
        this.scanner = scanner;
        this.chessBoard = new ChessBoard();
        this.chessBoardRenderer = new ChessBoardRenderer();
    }

    public boolean showMenu() {
        System.out.println("\n-- Postlogin Menu --");
        System.out.println("1. Help");
        System.out.println("2. Logout");
        System.out.println("3. Create Game");
        System.out.println("4. List Games");
        System.out.println("5. Play Game");
        System.out.println("6. Observe Game");
        System.out.println("7. Quit");

        int choice = getUserChoice();

        switch (choice) {
            case 1 -> displayHelp();
            case 2 -> {
                return !handleLogout();
            }
            case 3 -> handleCreateGame();
            case 4 -> handleListGames();
            case 5 -> handlePlayGame();
            case 6 -> handleObserveGame();
            case 7 -> quit();
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
        System.out.println(" - Play Game: Join a chess game.");
        System.out.println(" - Quit: Exit the program.");
    }

    private boolean handleLogout() {
        Map<String, Object> response = serverFacade.logout();
        return response.containsKey("success") && (boolean) response.get("success");
    }

    private void handleCreateGame() {
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine();

        Map<String, Object> response = serverFacade.createGame(gameName);
        if (response.containsKey("success") && (boolean) response.get("success")) {
            System.out.println("Game created successfully: " + gameName);
            chessBoard.resetBoard();
        } else {
            System.out.println("Game creation failed: " + response.get("error"));
        }
    }

    private void handleListGames() {
        Map<String, Object> response = serverFacade.listGames();
        if (response.containsKey("games")) {
            ListedGames = (List<Map<String, Object>>) response.get("games");
            if (ListedGames != null && !ListedGames.isEmpty()) {
                System.out.println("Available games:");
                int index = 1;
                for (Map<String, Object> game : ListedGames) {
                    String gameName = (String) game.getOrDefault("gameName", "Unnamed Game");
                    String whiteuser = (String) game.getOrDefault("whiteUsername", "[not in use]");
                    String blackuser = (String) game.getOrDefault("blackUsername", "[not in use]");
                    int gameID = ((Double) game.getOrDefault("gameID", -1)).intValue();
                    System.out.println(index + ": " + gameName + " white: " + whiteuser +  ", black: " + blackuser);
                    index++;
                }
            } else {
                System.out.println("No available games found.");
            }
        } else {
            System.out.println("Failed to retrieve games list: " + response.getOrDefault("error", "Unknown error"));
        }
    }

    public void handlePlayGame() {
        if (ListedGames == null || ListedGames.isEmpty()) {
            System.out.println("No available games. Please list games first.");
            return;
        }

        System.out.print("Enter the number of the game to join: ");
        int gameNumber = scanner.nextInt();
        scanner.nextLine();
        if(gameNumber < 1 || gameNumber > ListedGames.size()) {
            System.out.println("Invalid game number. Try again.");
            return;
        }
        Map<String, Object> selectedGame = ListedGames.get(gameNumber-1);

        int gameID = ((Double) selectedGame.get("gameID")).intValue();

        System.out.print("Enter player color (black/white): ");
        String playerColor = scanner.nextLine().trim().toLowerCase();

        if (!playerColor.equals("black") && !playerColor.equals("white")) {
            System.out.println("Invalid color choice. Please enter 'black' or 'white'.");
            return;
        }

        Map<String, Object> response = serverFacade.joinGame(String.valueOf(gameID), playerColor);
        if (response.containsKey("success") && (boolean) response.get("success")) {
            System.out.println("Successfully joined game with ID " + gameID + " as " + playerColor + ".");
            chessBoard.resetBoard();
            displayInitialBoard(chessBoard, true);
            displayInitialBoard(chessBoard, false);
        } else {
            System.out.println("Failed to join game: " + response.get("error"));
        }
    }

    public void displayInitialBoard(ChessBoard board, boolean whiteAtBottom) {
        System.out.println("Board with " + (whiteAtBottom ? "White" : "Black") + " at the bottom:");
        chessBoardRenderer.drawBoard(board, whiteAtBottom);
    }

    public void handleObserveGame() {
        if (ListedGames == null || ListedGames.isEmpty()) {
            System.out.println("No available games. Please list games first.");
            return;
        }

        System.out.print("Enter the game ID to observe: ");
        int gameNumber = scanner.nextInt();
        scanner.nextLine();

        Map<String, Object> selectedGame = null;
        for (Map<String, Object> game : ListedGames) {
            if (game.containsKey("gameID")) {
                int gameID = ((Double) game.getOrDefault("gameID", -1)).intValue();
                if (gameID == gameNumber) {
                    System.out.println("correct gameID");
                    selectedGame = game;
                    break;
                }
            }
        }

        if (selectedGame == null) {
            System.out.println("Invalid gameID");
            return;
        }

        int gameID = ((Double) selectedGame.get("gameID")).intValue();
        Map<String, Object> response = serverFacade.observeGame(String.valueOf(gameID));
        if (response.containsKey("error")) {
            System.out.println("Failed to observe game: " + response.get("error"));
        } else {
            displayBoardState(chessBoard, true);
            displayBoardState(chessBoard, false);
        }
    }


    public void displayBoardState(ChessBoard board, boolean whiteAtBottom) {
        System.out.println("Board with " + (whiteAtBottom ? "White" : "Black") + " at the bottom:");
        chessBoardRenderer.drawBoard(board, whiteAtBottom);
    }


    private void quit () {
            System.out.println("Exiting Chess Client. Goodbye!");
            System.exit(0);
        }
    }
