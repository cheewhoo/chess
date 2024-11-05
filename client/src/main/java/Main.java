import chess.*;
import ui.*;
import java.util.Scanner;

public class Main {
    private static ServerFacade serverFacade = new ServerFacade();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client");

        boolean isLoggedIn = false;

        while (true) {
            if (!isLoggedIn) {
                PreloginUI preloginUI = new PreloginUI(serverFacade, scanner);
                isLoggedIn = preloginUI.showMenu();
            } else {
                PostloginUI postloginUI = new PostloginUI(serverFacade, scanner);
                isLoggedIn = postloginUI.showMenu();
            }
        }
    }
}