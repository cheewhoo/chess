package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.*;
import dataaccess.UserDAO;
import model.DataAuth;
import model.DataUser;
import java.util.UUID;
public class Service_User {
    UserDAO userDAO;
    AuthDAO authDAO;
    public Service_User(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public DataAuth makeUser(DataUser userData) throws DataAccessException, UnauthorizedException,UserAlreadyExistsException {
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            throw new UnauthorizedException("Bad user data.");
        }
        if (userDAO.userExists(userData.username())) {
            throw new UserAlreadyExistsException("User already exists.");
        }
        userDAO.makeUser(userData);
        String authToken = UUID.randomUUID().toString();
        DataAuth authData = new DataAuth(userData.username(), authToken);
        authDAO.addAuthentication(authData);
        return authData;
    }
    public DataAuth loginUser(DataUser userinfo) throws DataAccessException {
        boolean userAuthenticated = userDAO.authUser(userinfo.username(), userinfo.password());
        if (userAuthenticated) {
            String authToken = UUID.randomUUID().toString();
            DataAuth authData = new DataAuth(userinfo.username(), authToken);
            authDAO.addAuthentication(authData);
            return authData;
        }
        else {
            throw new DataAccessException("Password is incorrect");
        }
    }
    public void logoutUser(String authToken) throws DataAccessException {
        authDAO.getAuthentication(authToken);
        authDAO.deleteAuthentication(authToken);
    }
    public void clearUsers() {
        userDAO.clear();
        authDAO.clear();
    }
}
