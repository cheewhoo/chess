package dataaccess;

import model.DataUser;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLUser implements UserDAO {
    private static final Logger LOGGER = Logger.getLogger(SQLUser.class.getName());

    public SQLUser() throws DataAccessException {
        initializeUserTable();
    }

    private void initializeUserTable() throws DataAccessException{
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(50) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(50) NOT NULL,
                PRIMARY KEY (username)
            )
        """;


        try (var conn = DatabaseManager.getConnection()) {
//            conn.setCatalog("chess");
            try (var createTableStatement = conn.prepareStatement(createTableSQL)) {
                createTableStatement.executeUpdate();

            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());

        }
    }

    @Override
    public DataUser getUser(String username) throws DataAccessException {
        String querySQL = "SELECT username, password, email FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(querySQL)) {

            statement.setString(1, username);
            try (var results = statement.executeQuery()) {
                if (results.next()) {
                    String user = results.getString("username");
                    String password = results.getString("password");
                    String email = results.getString("email");
                    return new DataUser(user, password, email);
                } else {
                    throw new DataAccessException("User not found: " + username);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user: " + username);
        }
    }


    public void makeUser(DataUser user) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        try (var conn = DatabaseManager.getConnection()){
            String insertSQL = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
                  var statement = conn.prepareStatement(insertSQL);
            statement.setString(1, user.username());
            statement.setString(2, hashedPassword);
            statement.setString(3, user.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("User already exists: " + user.username());
        }
    }


    @Override
    public boolean authUser(String username, String password) throws DataAccessException {
        DataUser user = getUser(username);
        return BCrypt.checkpw(password, user.password());
    }


    @Override
    public boolean userExists(String username) throws DataAccessException {
        String querySQL = "SELECT COUNT(*) FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(querySQL)) {

            statement.setString(1, username);
            try (var results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking user existence: " + username);
        }
        return false;
    }

    @Override
    public void clear() {
        String truncateSQL = "TRUNCATE user";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(truncateSQL)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to clear user table", e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
