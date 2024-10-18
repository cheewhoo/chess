package dataaccess;

import model.DataAuth;

public interface AuthDAO {
    DataAuth getAuthentication(String authToken) throws DataAccessException;
    void addAuthentication(DataAuth authData);
    void deleteAuthentication(String authToken);
    void clear();
}
