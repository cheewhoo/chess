package service;
import dataaccess.Auth_DAO;
import dataaccess.DataAccessException;
import dataaccess.User_DAO;
import model.Data_Auth;
import model.Data_User;
import java.util.UUID;

public class Service_User {User_DAO userDAO;
    Auth_DAO authDAO;
    public Service_User(User_DAO userDAO, Auth_DAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public Data_Auth makeUser(Data_User userData) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        Data_Auth authData = new Data_Auth(userData.username(), authToken);
        userDAO.makeUser(userData);
        authDAO.addAuthentication(authData);
        return authData;
    }
    public void clear() {
        userDAO.clear();
        authDAO.clear();
    }
}
