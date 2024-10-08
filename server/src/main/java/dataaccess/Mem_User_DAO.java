package dataaccess;
import model.Data_User;
import java.util.HashSet;


public class Mem_User_DAO implements User_DAO{
    private HashSet<Data_User> db;
    public Mem_User_DAO() {
        db = HashSet.newHashSet(16);
    }

    @Override
    public Data_User getUser(String username) throws DataAccessException {
        for (Data_User user : db) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("Unknown User: " + username);
    }

    @Override
    public void makeUser(String username, String password, String email) throws DataAccessException {
        try {
            getUser(username);
        }
        catch (DataAccessException e) {
            db.add(new Data_User(username, password, email));
            return;
        }
        throw new DataAccessException("User already exists: " + username);
    }

    @Override
    public void makeUser(Data_User user) throws DataAccessException {
        try {
            getUser(user.username());
        }
        catch (DataAccessException e) {
            db.add(user);
            return;
        }
        throw new DataAccessException("User already exists: " + user.username());
    }
    @Override
    public boolean authUser(String username, String password) throws DataAccessException {
        boolean knownuser = false;
        for (Data_User user : db) {
            if (user.username().equals(username)) {
                knownuser = true;
            }
            if (user.username().equals(username) &&
                    user.password().equals(password)) {
                return true;
            }
        }
        if (knownuser) {
            return false;
        }
        else {
            throw new DataAccessException("Unknown User: " + username);
        }
    }
    @Override
    public void clear() {
        db = HashSet.newHashSet(16);
    }

}
