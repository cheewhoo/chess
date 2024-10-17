package server;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.UserAlreadyExistsException;
import model.Data_Auth;
import model.Data_User;
import service.Service_User;
import spark.Request;
import spark.Response;
public class HandleUser {
    private final Service_User userService;
    public HandleUser(Service_User userService) {
        this.userService = userService;
    }
    public Object registerUser(Request req, Response resp) {
        Error_model errorModel = new Error_model("");
        try {
            Data_User userData = new Gson().fromJson(req.body(), Data_User.class);
            Data_Auth authData = userService.makeUser(userData);
            if (authData == null || userData.password() == null || userData.email() == null) {
                resp.status(400);
                errorModel = new Error_model("Error: bad request");
                return new Gson().toJson(errorModel);
            }
            resp.status(200);
            return new Gson().toJson(authData);
        } catch (UserAlreadyExistsException e) {
            resp.status(403);
            errorModel = new Error_model("Error: already taken");
            return new Gson().toJson(errorModel);
        } catch (JsonSyntaxException e) {
            resp.status(400);
            return "{ \"error\": \"Invalid request body.\" }";
        } catch (DataAccessException e) {
            resp.status(500);
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        } catch (UnauthorizedException e) {
            resp.status(400);
            errorModel = new Error_model("Error: " + e.getMessage());
            return new Gson().toJson(errorModel);
        }
    }

    public Object loginUser(Request req, Response resp) {
        Error_model errorModel = new Error_model("");
        try {
            Data_User userData = new Gson().fromJson(req.body(), Data_User.class);
            Data_Auth authData = userService.loginUser(userData);
            resp.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            resp.status(401);
            errorModel = new Error_model("Error: " + e.getMessage());
            return new Gson().toJson(errorModel);
        } catch (Exception e) {
            resp.status(500);
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        }
    }
    public Object logoutUser(Request req, Response resp) {
        Error_model errorModel = new Error_model("");
        try {
            String authToken = req.headers("authorization");
            userService.logoutUser(authToken);
            resp.status(200);
            return "{}";
        } catch (DataAccessException e) {
            resp.status(401);
            errorModel = new Error_model("Error: unauthorized");
            return new Gson().toJson(errorModel);
        } catch (Exception e) {
            resp.status(500);
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        }
    }
}

