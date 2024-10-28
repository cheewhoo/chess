package dataaccess;

import chess.ChessGame;
import model.DataGame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;


public class SQLGame implements GameDAO {

    public SQLGame() throws DataAccessException{
        initializeDatabase();
    }

    private void initializeDatabase() throws DataAccessException{
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT PRIMARY KEY,
                whiteUsername VARCHAR(50),
                blackUsername VARCHAR(50),
                gameName VARCHAR(50),
                chessGame TEXT
            )
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
//            conn.setCatalog("chess");
            stmt.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public HashSet<DataGame> gamesList() {
        HashSet<DataGame> allGames = new HashSet<>();
        String query = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM game";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                allGames.add(constructGame(rs));
            }
        } catch (SQLException | DataAccessException e) {
            return null;
        }

        return allGames;
    }


    @Override
    public DataGame getGame(int gameID) throws DataAccessException {
        String query = "SELECT whiteUsername, blackUsername, gameName, chessGame FROM game WHERE gameID = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, gameID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return constructGame(gameID, rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to find game with ID: " + gameID);
        }

        return null;
    }

    @Override
    public void makeGame(DataGame game) {
        String insertSQL = """
            INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, chessGame)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            fillStatement(stmt, game);
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            // Silent failure
        }
    }

    @Override
    public boolean gameExists(int gameID) {
        String query = "SELECT 1 FROM game WHERE gameID = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, gameID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | DataAccessException e) {
            return false;
        }
    }

    @Override
    public void updateGame(DataGame game) {
        String updateSQL = """
        UPDATE game
        SET whiteUsername = ?, blackUsername = ?, gameName = ?, chessGame = ?
        WHERE gameID = ?
    """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

            stmt.setString(1, game.whiteUsername());
            stmt.setString(2, game.blackUsername());
            stmt.setString(3, game.gameName());
            stmt.setString(4, game.game() != null ? game.game().toString() : null);
            stmt.setInt(5, game.gameID());
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            // Silent failure
        }
    }


    @Override
    public void clear() {
        String clearSQL = "TRUNCATE TABLE game";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(clearSQL)) {
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            // Silent failure
        }
    }

    private DataGame constructGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        return constructGame(gameID, rs);
    }

    private DataGame constructGame(int gameID, ResultSet rs) throws SQLException {
        String whiteUser = rs.getString("whiteUsername");
        String blackUser = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String chessGameData = rs.getString("chessGame");

        return new DataGame(gameID, whiteUser, blackUser, gameName, new ChessGame());
    }

    private void fillStatement(PreparedStatement stmt, DataGame game) throws SQLException {
        stmt.setInt(1, game.gameID());
        stmt.setString(2, game.whiteUsername());
        stmt.setString(3, game.blackUsername());
        stmt.setString(4, game.gameName());
        stmt.setString(5, game.game() != null ? game.game().toString() : null);
    }
}
