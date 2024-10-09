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
    Service_User userService;
    public HandleUser(Service_User userService) {
        this.userService = userService;
    }
    public Object register(Request req, Response resp) {
        try {
            Data_User userData = new Gson().fromJson(req.body(), Data_User.class);
            Data_Auth authData = userService.makeUser(userData);
            if (authData == null) {
                resp.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            } else {
                resp.status(200);
                return new Gson().toJson(authData);
            }
        } catch (DataAccessException e) {
            resp.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        } catch (JsonSyntaxException e) {
            resp.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"message\": \"Error: %s\" }".formatted(e.getMessage());
        }
    }
    public Object login(Request req, Response resp) {
        try {
            Data_User userData = new Gson().fromJson(req.body(), Data_User.class);
            Data_Auth authData = userService.loginUser(userData);
            resp.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            resp.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"message\": \"Error: %s\" }".formatted(e.getMessage());
        }
    }
    public Object logout(Request req, Response resp) {
        try {
            String authToken = req.headers("authorization");
            userService.logoutUser(authToken);
            resp.status(200);
            return "{}";
        } catch (DataAccessException e) {
            resp.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (Exception e) {
            resp.status(500);
            return "{ \"message\": \"Error: %s\" }".formatted(e.getMessage());
        }
    }
}

