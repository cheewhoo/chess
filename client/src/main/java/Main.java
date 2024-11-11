import ui.*;
import java.util.Scanner;

public class Main {
    private static ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client");

        boolean isLoggedIn = false;
        PreloginUI preloginUI = new PreloginUI(serverFacade, scanner);
        PostloginUI postloginUI = new PostloginUI(serverFacade, scanner);
        while (true) {
            if (!isLoggedIn) {
                isLoggedIn = preloginUI.showMenu();
            } else {
                isLoggedIn = postloginUI.showMenu();
            }
        }
    }
}