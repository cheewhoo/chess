package server;
import spark.*;
import service.Service_User;
import service.Service_Game;
import com.google.gson.Gson;
import dataaccess.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        Spark.delete("/db", this::clear);
        Spark.post("/user", userHandler::registerUser);
        Spark.post("/session", userHandler::loginUser);
        Spark.delete("/session", userHandler::logoutUser);
        Spark.get("/game", gameHandler::Games_lst);
        Spark.post("/game", gameHandler::makeGame);
        Spark.put("/game", gameHandler::joinExisitngGame);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    User_DAO userDAO;
    Auth_DAO authDAO;
    Game_DAO gameDAO;
    Service_User serviceuser;
    Service_Game servicegame;
    HandleUser userHandler;
    HandleGame gameHandler;
    public Server() {
        userDAO = new Mem_User_DAO();
        authDAO = new Mem_Auth_DAO();
        gameDAO = new Mem_Game_DAO();
        serviceuser = new Service_User(userDAO, authDAO);
        servicegame = new Service_Game(gameDAO, authDAO);
        userHandler = new HandleUser(serviceuser);
        gameHandler = new HandleGame(servicegame);
    }

    private Object clear(Request req, Response resp) {
        try {
            serviceuser.clearUsers();
            servicegame.clearGames();
            resp.status(200);
            return "{}";
        }
        catch (Exception e) {
            resp.status(500);
            return "{ \"message\": \"Error: %s\"}".formatted(new Gson().toJson(e.getMessage()));
        }
    }
}
