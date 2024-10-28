package server;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.UserAlreadyExistsException;
import model.DataAuth;
import model.DataUser;
import service.ServiceUser;
import spark.Request;
import spark.Response;
public class HandleUser {
    private final ServiceUser userService;
    public HandleUser(ServiceUser userService) {
        this.userService = userService;
    }
    public Object registerUser(Request req, Response resp) {
        ErrorModel errorModel = new ErrorModel("");
        try {
            DataUser userData = new Gson().fromJson(req.body(), DataUser.class);
            DataAuth authData = userService.makeUser(userData);
            if (authData == null || userData.password() == null || userData.email() == null) {
                resp.status(400);
                errorModel = new ErrorModel("Error: bad request");
                return new Gson().toJson(errorModel);
            }
            resp.status(200);
            return new Gson().toJson(authData);
        } catch (UserAlreadyExistsException e) {
            resp.status(403);
            errorModel = new ErrorModel("Error: already taken");
            return new Gson().toJson(errorModel);
        } catch (JsonSyntaxException e) {
            resp.status(400);
            errorModel = new ErrorModel("Error: bad request");
            return new Gson().toJson(errorModel);
        } catch (DataAccessException e) {
            resp.status(500);
            return "{ \"message\": \"Error: \"" + e.getMessage() + "\" }";
        } catch (UnauthorizedException e) {
            resp.status(400);
            errorModel = new ErrorModel("Error: " + e.getMessage());
            return new Gson().toJson(errorModel);
        }
    }

    public Object loginUser(Request req, Response resp) {
        ErrorModel errorModel = new ErrorModel("");
        try {
            DataUser userData = new Gson().fromJson(req.body(), DataUser.class);
            DataAuth authData = userService.loginUser(userData);
            resp.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            resp.status(401);
            errorModel = new ErrorModel("Error: " + e.getMessage());
            return new Gson().toJson(errorModel);
        } catch (Exception e) {
            resp.status(500);
            return "{ \"message\": \"Error: \"" + e.getMessage() + "\" }";
        }
    }
    public Object logoutUser(Request req, Response resp) {
        ErrorModel errorModel = new ErrorModel("");
        try {
            String authToken = req.headers("authorization");
            userService.logoutUser(authToken);
            resp.status(200);
            return "{}";
        } catch (DataAccessException e) {
            resp.status(401);
            errorModel = new ErrorModel("Error: unauthorized");
            return new Gson().toJson(errorModel);
        } catch (Exception e) {
            resp.status(500);
            return "{ \"message\": \"Error: \"" + e.getMessage() + "\" }";
        }
    }
}

