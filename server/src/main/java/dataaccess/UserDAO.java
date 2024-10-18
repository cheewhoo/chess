package dataaccess;

import model.DataUser;

public interface UserDAO {
    DataUser getUser(String username) throws DataAccessException;
    void makeUser(DataUser user) throws DataAccessException, UserAlreadyExistsException;
    boolean authUser(String username, String password) throws DataAccessException;
    boolean userExists(String username) throws DataAccessException;
    void clear();
}
