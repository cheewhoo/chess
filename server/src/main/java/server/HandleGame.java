package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.Game_DAO;
import dataaccess.UnauthorizedException;
import model.Data_Game;
import service.Service_Game;
import spark.Request;
import spark.Response;
import java.util.HashSet;

public class HandleGame {
    Service_Game gameService;
    public HandleGame(Service_Game gameService) {
        this.gameService = gameService;
    }
    public Object Games_lst(Request req, Response resp) {
        try {
            String authenticationToken = req.headers("authorization");
            HashSet<Data_Game> games = gameService.Games_lst(authenticationToken);
            resp.status(200);
            return "{ \"games\": %s}".formatted(new Gson().toJson(games));
        } catch (DataAccessException e) {
            resp.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"message\": \"Error: %s\" }".formatted(e.getMessage());
        }
    }
    public Object makeGame(Request req, Response resp) {
        if (!req.body().contains("\"gameName\":")) {
            resp.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
        try {
            String authToken = req.headers("authorization");
            int gameID =  gameService.makeGame(authToken);
            resp.status(200);
            return "{ \"gameID\": %d }".formatted(gameID);
        } catch (DataAccessException e) {
            resp.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"message\": \"Error: %s\" }".formatted(e.getMessage());
        }
    }
    public Object joinGame(Request req, Response resp) {
        if (!req.body().contains("\"gameID\":")) {
            resp.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
        try {
            String authToken = req.headers("authorization");
            record JoinGameData(String playerColor, int gameID) {}
            JoinGameData joinData = new Gson().fromJson(req.body(), JoinGameData.class);
            int joinStatus =  gameService.joinGame(authToken, joinData.gameID(), joinData.playerColor());
            if (joinStatus == 0) {
                resp.status(200);
                return "{}";
            } else if (joinStatus == 1) {
                resp.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            } else if (joinStatus == 2) {
                resp.status(403);
                return "{ \"message\": \"Error: already taken\" }";
            }
            resp.status(200);
            return "{}";
        } catch (DataAccessException e) {
            resp.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } catch (UnauthorizedException e) {
            resp.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"message\": \"Error: %s\" }".formatted(e.getMessage());
        }
    }
}
