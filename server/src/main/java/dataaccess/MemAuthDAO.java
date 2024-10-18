package dataaccess;

import model.DataAuth;
import java.util.HashSet;
import java.util.Iterator;

public class MemAuthDAO implements AuthDAO {
    private HashSet<DataAuth> authDatabase;

    public MemAuthDAO() {
        this.authDatabase = new HashSet<>();
    }

    @Override
    public void addAuthentication(DataAuth authData) {
        if (!authDatabase.contains(authData)) {
            authDatabase.add(authData);
        }
    }

    @Override
    public DataAuth getAuthentication(String authToken) throws DataAccessException {
        DataAuth foundAuth = authDatabase.stream()
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
        Iterator<DataAuth> iterator = authDatabase.iterator();
        while (iterator.hasNext()) {
            DataAuth authData = iterator.next();
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
