package dataaccess;

import model.DataAuth;

public interface AuthDAO {
    DataAuth getAuthentication(String authToken) throws DataAccessException;
    void addAuthentication(DataAuth authData) throws DataAccessException;
    void deleteAuthentication(String authToken);
    void clear();
}
