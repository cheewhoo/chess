package server;
import dataaccess.Game_DAO;
import service.Service_Game;

public class HandleGame {
    Service_Game gameService;
    public HandleGame(Service_Game gameService) {
        this.gameService = gameService;
    }
}
