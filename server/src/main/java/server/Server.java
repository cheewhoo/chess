package server;
import spark.*;
import service.ServiceUser;
import service.ServiceGame;
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
        Spark.get("/game", gameHandler::gamesList);
        Spark.post("/game", gameHandler::makeGame);
        Spark.put("/game", gameHandler::joinExisitngGame);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    ServiceUser serviceuser;
    ServiceGame servicegame;
    HandleUser userHandler;
    HandleGame gameHandler;
    public Server(){
//        userDAO = new MemUserDAO();
        try {
            userDAO = new SQLUser();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
//        authDAO = new MemAuthDAO();
        try{
            authDAO = new SQLAuth();
        } catch (DataAccessException e){
            throw new RuntimeException(e.getMessage());

        }
//        gameDAO = new MemGameDAO();
        try{
            gameDAO = new SQLGame();
        } catch (DataAccessException e){
            throw new RuntimeException(e.getMessage());
        }
        serviceuser = new ServiceUser(userDAO, authDAO);
        servicegame = new ServiceGame(gameDAO, authDAO);
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
            ErrorModel errorModel = new ErrorModel("Error: " + e.getMessage());
            return new Gson().toJson(errorModel);
        }
    }
}
