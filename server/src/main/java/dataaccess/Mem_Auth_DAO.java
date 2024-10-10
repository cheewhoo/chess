package dataaccess;
import model.Data_Auth;
import java.util.HashSet;



public class Mem_Auth_DAO implements Auth_DAO{
    HashSet<Data_Auth> db;
    public Mem_Auth_DAO() {
        db = HashSet.newHashSet(16);
    }
    @Override
    public void addAuthentication(Data_Auth authData) {
        db.add(authData);
    }
    @Override
    public Data_Auth getAuthentication(String authToken) throws DataAccessException {
        for (Data_Auth authData : db) {
            if (authData.authenticationToken().equals(authToken)) {
                return authData;
            }
        }
        throw new DataAccessException("Unknown authToken: " + authToken);
    }
    @Override
    public void deleteAuthentication(String authToken) {
        for (Data_Auth authData : db) {
            if (authData.authenticationToken().equals(authToken)) {
                db.remove(authData);
                break;
            }
        }
    }

    @Override
    public void clear() {
        db = HashSet.newHashSet(16);
    }
}
