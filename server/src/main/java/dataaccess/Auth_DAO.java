package dataaccess;

import model.Data_Auth;

public interface Auth_DAO {
    void addAuthentication(String authToken, String username);
    void addAuthentication(Data_Auth authData);
    void deleteAuthentication(String authToken);
    Data_Auth getAuthentication(String authToken) throws DataAccessException;
    void clear();
}
