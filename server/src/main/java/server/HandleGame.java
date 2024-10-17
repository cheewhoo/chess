package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.Data_Game;
import service.Service_Game;
import spark.Request;
import spark.Response;
import java.util.HashSet;

public class HandleGame {
    private final Service_Game gameService;
    public HandleGame(Service_Game gameService) {
        this.gameService = gameService;
    }
    public Object Games_lst(Request req, Response resp) {
        Error_model errorModel = new Error_model("");
        try {
            String authenticationToken = req.headers("authorization");
            HashSet<Data_Game> gamesList = gameService.Games_lst(authenticationToken);

            resp.status(200);
            return new Gson().toJson(new ResponseWrapper(gamesList));
        } catch (UnauthorizedException e) {
            resp.status(401);
            errorModel = new Error_model("Error: unauthorized");
            return new Gson().toJson(errorModel);
        } catch (Exception e) {
            resp.status(500);
            return "{ \"Error\": \"" + e.getMessage() + "\" }";
        }
    }

    private static class ResponseWrapper {
        private final HashSet<Data_Game> games;

        public ResponseWrapper(HashSet<Data_Game> games) {
            this.games = games;
        }

        public HashSet<Data_Game> getGames() {
            return games;
        }
    }

    public Object makeGame(Request req, Response resp) {
        Error_model errorModel = new Error_model("");
        if (!req.body().contains("\"gameName\":")) {
            resp.status(400);
            errorModel = new Error_model("Error: bad request");
            return new Gson().toJson(errorModel);
        }
        try {
            String authToken = req.headers("authorization");
            int newgameID =  gameService.makeGame(authToken);
            resp.status(200);
            return "{ \"gameID\": " + newgameID + " }";
        } catch (UnauthorizedException e) {
            resp.status(401);
            errorModel = new Error_model("Error: unauthorized");
            return new Gson().toJson(errorModel);
        } catch (Exception e) {
            resp.status(500);
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        }
    }
    public Object joinExisitngGame(Request req, Response resp) {
        Error_model errorModel = new Error_model("");
        if (!req.body().contains("\"gameID\":")) {
            resp.status(400);
            errorModel = new Error_model("Error: bad request");
            return new Gson().toJson(errorModel);
        }
        try {
            String authToken = req.headers("Authorization");
            var joinData = new Gson().fromJson(req.body(), JoinGameData.class);
            int joinResult = gameService.joinGame(authToken, joinData.gameID(), joinData.playerColor());
            if(joinData.playerColor == null){
                resp.status(400);
                errorModel = new Error_model("Error: bad request");
                return new Gson().toJson(errorModel);

            }
            if (joinResult == 0) {
                resp.status(200);
                return "{}";
            } else if (joinResult == 1) {
                resp.status(400);
                return "{ \"error\": \"Bad request.\" }";
            } else {
                resp.status(403);
                errorModel = new Error_model("Error: already taken");
                return new Gson().toJson(errorModel);
            }
        } catch (DataAccessException e) {
            resp.status(400);
            return "{ \"error\": \"Bad request.\" }";
        } catch (UnauthorizedException e) {
            resp.status(401);
            return "{ \"error\": \"Not authorized.\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        }
    }

    private record JoinGameData(String playerColor, int gameID) {
    }
}
