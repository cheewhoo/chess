package dataaccess;
import model.DataUser;
import java.util.HashSet;
import java.util.Optional;

public class MemUserDAO implements UserDAO {
    private HashSet<DataUser> userDatabase;

    public MemUserDAO() {
        userDatabase = new HashSet<>();
    }

    @Override
    public DataUser getUser(String username) throws DataAccessException {
        return userDatabase.stream()
                .filter(user -> user.username().equals(username))
                .findFirst()
                .orElseThrow(() -> new DataAccessException("User not found: " + username));
    }

    @Override
    public void makeUser(DataUser user) throws UserAlreadyExistsException {
        if (userDatabase.stream().anyMatch(existingUser -> existingUser.username().equals(user.username()))) {
            throw new UserAlreadyExistsException("User already exists: " + user.username());
        }
        userDatabase.add(user);
    }

    @Override
    public boolean authUser(String username, String password) throws DataAccessException {
        Optional<DataUser> user = userDatabase.stream()
                .filter(existingUser -> existingUser.username().equals(username))
                .findFirst();

        if (user.isPresent()) {
            return user.get().password().equals(password);
        } else {
            throw new DataAccessException("Unknown User: " + username);
        }
    }

    @Override
    public boolean userExists(String username) throws DataAccessException {
        if (username == null || username.isEmpty()) {
            throw new DataAccessException("Username cannot be null or empty.");
        }
        return userDatabase.stream()
                .anyMatch(existingUser -> existingUser.username().equals(username));
    }

    @Override
    public void clear() {
        userDatabase.clear();
    }
}
