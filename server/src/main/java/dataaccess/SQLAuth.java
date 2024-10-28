package dataaccess;

import model.DataAuth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuth implements AuthDAO {

    public SQLAuth() throws DataAccessException{
        setupAuthTable();
    }

    public void setupAuthTable() throws DataAccessException{
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(50) NOT NULL PRIMARY KEY,
                username VARCHAR(50) NOT NULL
            )
        """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public DataAuth getAuthentication(String authToken) throws DataAccessException {
        String query = "SELECT username FROM auth WHERE authToken = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, authToken);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new DataAuth(resultSet.getString("username"), authToken);
                } else {
                    throw new DataAccessException("No authentication found for token: " + authToken);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve authentication for token: " + authToken);
        }
    }

    public void addAuthentication(DataAuth auth) throws DataAccessException {
        String sql = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, auth.username());
            stmt.setString(2, auth.authToken());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not add authentication");
        }
    }


    @Override
    public void deleteAuthentication(String authToken) {
        String deleteQuery = "DELETE FROM auth WHERE authToken = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                throw new DataAccessException("Could not delete authentication for token: " + authToken);
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        String truncateQuery = "TRUNCATE TABLE auth";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(truncateQuery)) {
            statement.execute();
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("doesn't exist")) {
                System.out.println("Table 'auth' doesn't exist, nothing to clear.");
            } else {
                throw new RuntimeException("Failed to clear authentication data: " + e.getMessage(), e);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
