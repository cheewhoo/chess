package dataaccess;

import model.Data_User;

public interface UserDAO {
    Data_User getUser(String username) throws DataAccessException;
    void makeUser(Data_User user) throws DataAccessException, UserAlreadyExistsException;
    boolean authUser(String username, String password) throws DataAccessException;
    boolean userExists(String username) throws DataAccessException;
    void clear();
}
