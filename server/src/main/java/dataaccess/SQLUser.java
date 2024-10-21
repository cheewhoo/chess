package dataaccess;

import model.DataUser;
import java.sql.SQLException;

public class SQLUser implements UserDAO {
    public SQLUser() {
        initializeUserTable();
    }

    private void initializeUserTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255),
                PRIMARY KEY (username)
            )
        """;

        try (var conn = DatabaseManager.getConnection()) {
            conn.setCatalog("chess");
            try (var createTableStatement = conn.prepareStatement(createTableSQL)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize user table: " + e.getMessage(), e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
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
                    String password = results.getString("password");
                    String email = results.getString("email");
                    return new DataUser(username, password, email);
                } else {
                    throw new DataAccessException("User not found: " + username);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user: " + username);
        }
    }

    @Override
    public void makeUser(DataUser user) throws DataAccessException {
        String insertSQL = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(insertSQL)) {
            statement.setString(1, user.username());
            statement.setString(2, user.password());
            statement.setString(3, user.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("User already exists: " + user.username());
        }
    }

    @Override
    public boolean authUser(String username, String password) throws DataAccessException {
        DataUser user = getUser(username);
        return user.password().equals(password);
    }

    @Override
    public boolean userExists(String username) throws DataAccessException {
        String querySQL = "SELECT COUNT(*) FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(querySQL)) {

            statement.setString(1, username);
            try (var results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1) > 0; // Return true if count > 0
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking user existence: " + username);
        }
        return false; // User does not exist
    }

    @Override
    public void clear() {
        String truncateSQL = "TRUNCATE user";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(truncateSQL)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            // Log the exception if needed, but suppress it to maintain the method contract
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
