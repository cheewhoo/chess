package dataaccess;

import model.DataAuth;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuth implements AuthDAO {

    public SQLAuth() {
        initializeAuthTable();
    }

    private void initializeAuthTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(50) NOT NULL,
                username VARCHAR(50) NOT NULL,
                PRIMARY KEY (authToken)
            )
        """;

        try (var conn = DatabaseManager.getConnection();
             var createTableStatement = conn.prepareStatement(createTableSQL)) {
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize auth table: " + e.getMessage(), e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataAuth getAuthentication(String authToken) throws DataAccessException {
        String querySQL = "SELECT authToken, username FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(querySQL)) {

            statement.setString(1, authToken);
            try (var results = statement.executeQuery()) {
                if (results.next()) {
                    String username = results.getString("username");
                    return new DataAuth(authToken, username);
                } else {
                    throw new DataAccessException("Authentication not found for token: " + authToken);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving authentication: " + authToken);
        }
    }

    @Override
    public void addAuthentication(DataAuth authData) {
        String insertSQL = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(insertSQL)) {

            statement.setString(1, authData.authToken());
            statement.setString(2, authData.username());
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            try {
                throw new DataAccessException("Failed to add authentication: " + authData.authToken());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void deleteAuthentication(String authToken) {
        String deleteSQL = "DELETE FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(deleteSQL)) {

            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            try {
                throw new DataAccessException("Failed to delete authentication: " + authToken);
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void clear() {
        String truncateSQL = "TRUNCATE auth";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(truncateSQL)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear authentication data: " + e.getMessage(), e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
