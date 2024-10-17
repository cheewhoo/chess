package service;
import dataaccess.Auth_DAO;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.*;
import dataaccess.User_DAO;
import model.Data_Auth;
import model.Data_User;
import java.util.UUID;
public class Service_User {
    User_DAO userDAO;
    Auth_DAO authDAO;
    public Service_User(User_DAO userDAO, Auth_DAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public Data_Auth makeUser(Data_User userData) throws DataAccessException, UnauthorizedException,UserAlreadyExistsException {
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            throw new UnauthorizedException("Bad user data.");
        }
        if (userDAO.userExists(userData.username())) {
            throw new UserAlreadyExistsException("User already exists.");
        }
        userDAO.makeUser(userData);
        String authToken = UUID.randomUUID().toString();
        Data_Auth authData = new Data_Auth(userData.username(), authToken);
        authDAO.addAuthentication(authData);
        return authData;
    }
    public Data_Auth loginUser(Data_User userinfo) throws DataAccessException {
        boolean userAuthenticated = userDAO.authUser(userinfo.username(), userinfo.password());
        if (userAuthenticated) {
            String authToken = UUID.randomUUID().toString();
            Data_Auth authData = new Data_Auth(userinfo.username(), authToken);
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
