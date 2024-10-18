package dataaccess;

import model.Data_Auth;
import java.util.HashSet;
import java.util.Iterator;

public class MemAuthDAO implements AuthDAO {
    private HashSet<Data_Auth> authDatabase;

    public MemAuthDAO() {
        this.authDatabase = new HashSet<>();
    }

    @Override
    public void addAuthentication(Data_Auth authData) {
        if (!authDatabase.contains(authData)) {
            authDatabase.add(authData);
        }
    }

    @Override
    public Data_Auth getAuthentication(String authToken) throws DataAccessException {
        Data_Auth foundAuth = authDatabase.stream()
                .filter(authData -> authData.authToken().equals(authToken))
                .findAny()
                .orElse(null);

        if (foundAuth == null) {
            throw new DataAccessException("Authentication token not found: " + authToken);
        }
        return foundAuth;
    }

    @Override
    public void deleteAuthentication(String authToken) {
        Iterator<Data_Auth> iterator = authDatabase.iterator();
        while (iterator.hasNext()) {
            Data_Auth authData = iterator.next();
            if (authData.authToken().equals(authToken)) {
                iterator.remove();
                return;
            }
        }
    }

    @Override
    public void clear() {
        authDatabase.clear();
    }
}
