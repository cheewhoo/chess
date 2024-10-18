package dataaccess;

import model.Data_Auth;

public interface AuthDAO {
    Data_Auth getAuthentication(String authToken) throws DataAccessException;
    void addAuthentication(Data_Auth authData);
    void deleteAuthentication(String authToken);
    void clear();
}
