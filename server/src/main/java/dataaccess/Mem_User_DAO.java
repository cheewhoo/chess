package dataaccess;

import model.Data_User;
import java.util.HashSet;
import java.util.Optional;

public class Mem_User_DAO implements User_DAO {
    private HashSet<Data_User> userDatabase;

    public Mem_User_DAO() {
        userDatabase = new HashSet<>();
    }

    @Override
    public Data_User getUser(String username) throws DataAccessException {
        return userDatabase.stream()
                .filter(user -> user.username().equals(username))
                .findFirst()
                .orElseThrow(() -> new DataAccessException("User not found: " + username));
    }

    @Override
    public void makeUser(Data_User user) throws DataAccessException {
        if (userDatabase.stream().anyMatch(existingUser -> existingUser.username().equals(user.username()))) {
            throw new DataAccessException("User already exists: " + user.username());
        }
        userDatabase.add(user);
    }

    @Override
    public boolean authUser(String username, String password) throws DataAccessException {
        Optional<Data_User> user = userDatabase.stream()
                .filter(existingUser -> existingUser.username().equals(username))
                .findFirst();

        if (user.isPresent()) {
            return user.get().password().equals(password);
        } else {
            throw new DataAccessException("Unknown User: " + username);
        }
    }

    @Override
    public void clear() {
        userDatabase.clear();
    }
}
