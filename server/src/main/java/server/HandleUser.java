package server;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
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
        try {
            Data_User userData = new Gson().fromJson(req.body(), Data_User.class);
            Data_Auth authData = userService.makeUser(userData);
            if (authData == null) {
                resp.status(400);
                return "{ \"error\": \"Invalid request.\" }";
            }
            resp.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            resp.status(403);
            return "{ \"error\": \"Username already exists.\" }";
        } catch (JsonSyntaxException e) {
            resp.status(400);
            return "{ \"error\": \"Invalid request body.\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        }
    }
    public Object loginUser(Request req, Response resp) {
        try {
            Data_User userData = new Gson().fromJson(req.body(), Data_User.class);
            Data_Auth authData = userService.loginUser(userData);
            resp.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            resp.status(401);
            return "{ \"error\": \"Unauthorized.\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        }
    }
    public Object logoutUser(Request req, Response resp) {
        try {
            String authToken = req.headers("authorization");
            userService.logoutUser(authToken);
            resp.status(200);
            return "{}";
        } catch (DataAccessException e) {
            resp.status(401);
            return "{ \"error\": \"Unauthorized.\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        }
    }
}

