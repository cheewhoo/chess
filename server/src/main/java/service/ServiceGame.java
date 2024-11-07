package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UnauthorizedException;
import model.DataAuth;
import model.DataGame;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ServiceGame {
    static GameDAO gameDAO;
    AuthDAO authDAO;
    public ServiceGame(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public static void clearGames() {
        gameDAO.clear();
    }
    public HashSet<DataGame> gamesList(String authToken) throws UnauthorizedException {
        try {
            authDAO.getAuthentication(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Invalid authToken");
        }
        return gameDAO.gamesList();
    }
    public int makeGame(String authToken, String gameName) throws UnauthorizedException, DataAccessException {
        DataAuth authData;
        try {
            authData = authDAO.getAuthentication(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Invalid authToken");
        }
        String username = authData.username();
        int newGameID;
        do {
            newGameID = ThreadLocalRandom.current().nextInt(1, 10000);
        } while (gameDAO.gameExists(newGameID));
        DataGame newGame = new DataGame(newGameID, null, null, gameName, null);
        gameDAO.makeGame(newGame);
        return newGameID;
    }


    public int joinGame(String authToken, int gameID, String color) throws UnauthorizedException, DataAccessException {
        DataGame gameData;
        DataAuth authData;
        try {
            authData = authDAO.getAuthentication(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Invalid authToken");
        }
        if (gameDAO.gameExists(gameID)) {
            gameData = gameDAO.getGame(gameID);
        } else{
            return 1;
        }
        String whiteUser = gameData.whiteUsername();
        String blackUser = gameData.blackUsername();
        if (Objects.equals(color, "WHITE")) {
            if (whiteUser != null){
                return 2;
            }
            else{
                whiteUser = authData.username();
            }
        } else if (Objects.equals(color, "BLACK")) {
            if (blackUser != null){
                return 2;
            }
            else{
                blackUser = authData.username();
            }
        } else if (color != null){
            return 1;
        }
        gameDAO.updateGame(new DataGame(gameID, whiteUser, blackUser, gameData.gameName(), gameData.game()));
        return 0;
    }
}
