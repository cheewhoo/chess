package service;
import dataaccess.Game_DAO

public class Service_Game {
    Game_DAO gameDAO;
    public Service_Game(Game_DAO gameDAO) {
        this.gameDAO = gameDAO;
    }
    public void clear() {
        gameDAO.clear();
    }
}
