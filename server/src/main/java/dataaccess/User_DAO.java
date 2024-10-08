package dataaccess;

import model.Data_User;

public interface User_DAO {
    Data_User getUser(String username) throws DataAccessException;
    void makeUser(String username, String password, String email) throws DataAccessException;
    void makeUser(Data_User user) throws DataAccessException;
    boolean authUser(String username, String password) throws DataAccessException;
    void clear();
}
