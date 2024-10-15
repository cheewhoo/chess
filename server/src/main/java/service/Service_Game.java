package service;

import dataaccess.Auth_DAO;
import dataaccess.DataAccessException;
import dataaccess.Game_DAO;
import dataaccess.UnauthorizedException;
import model.Data_Auth;
import model.Data_Game;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Service_Game{
    Game_DAO gameDAO;
    Auth_DAO authDAO;
    public Service_Game(Game_DAO gameDAO, Auth_DAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public void clearGames() {
        gameDAO.clear();
    }
    public HashSet<Data_Game> Games_lst(String authToken) throws UnauthorizedException {
        try {
            authDAO.getAuthentication(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Invalid authToken");
        }
        return gameDAO.Games_lst();
    }
    public int makeGame(String authToken) throws DataAccessException {
        authDAO.getAuthentication(authToken);
        int newgameID;
        do {
            newgameID = ThreadLocalRandom.current().nextInt(1, 10000);
        } while (gameDAO.gameExists(newgameID));
        gameDAO.makeGame(new Data_Game(newgameID, null, null, null, null));
        return newgameID;
    }
    public int joinGame(String authToken, int gameID, String color) throws UnauthorizedException, DataAccessException {
        Data_Game gameData;
        Data_Auth authData;
        try {
            authData = authDAO.getAuthentication(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Invalid authToken");
        }
        if (gameDAO.gameExists(gameID)) {
            gameData = gameDAO.getGame(gameID);
        } else return 1;
        String whiteUser = gameData.whiteUser();
        String blackUser = gameData.blackUser();
        if (Objects.equals(color, "WHITE")) {
            if (whiteUser != null) return 2;
            else whiteUser = authData.username();
        } else if (Objects.equals(color, "BLACK")) {
            if (blackUser != null) return 2;
            else blackUser = authData.username();
        } else if (color != null) return 1;
        gameDAO.updateGame(new Data_Game(gameID, whiteUser, blackUser, gameData.gameName(), gameData.game()));
        return 0;
    }
}
