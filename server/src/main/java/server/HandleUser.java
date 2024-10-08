package server;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.Auth_DAO;
import dataaccess.DataAccessException;
import dataaccess.User_DAO;
import model.Data_Auth;
import model.Data_User;
import service.Service_User;
import spark.Request;
import spark.Response;
import java.util.UUID;

public class HandleUser {
    Service_User userService;
    public Service_User(Service_User userService) {
        this.userService = userService;
    }
    public Object register(Request req, Response resp) {
        try {
            Data_User userData = new Gson().fromJson(req.body(), Data_User.class);
            Data_Auth authData = userService.makeUser(userData);
            resp.status(200);
            return new Gson().toJson(authData);
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
        return null;
    }

}
