package dataaccess.sqltests;
import dataaccess.*;
import chess.ChessGame;
import model.DataGame;
import org.junit.jupiter.api.*;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SQLGameTest {

    private SQLGame gameDAO;

    @BeforeAll
    public void setup() throws DataAccessException {
        gameDAO = new SQLGame();
    }

    @BeforeEach
    public void clearDatabase() {
        gameDAO.clear();
    }

    @Test
    public void makeGameWorks() {
        DataGame game = new DataGame(1, "whitePlayer", "blackPlayer", "Epic Match", new ChessGame());
        gameDAO.makeGame(game);

        assertTrue(gameDAO.gameExists(1), "Game should exist after being added.");
    }

    @Test
    public void makeGameFails() {
        DataGame game1 = new DataGame(1, "whitePlayer1", "blackPlayer1", "Match1", new ChessGame());
        DataGame game2 = new DataGame(1, "whitePlayer2", "blackPlayer2", "Match2", new ChessGame());
        gameDAO.makeGame(game1);
        gameDAO.makeGame(game2);
        DataGame fetchedGame = null;
        try {
            fetchedGame = gameDAO.getGame(1);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Match1", fetchedGame.gameName(), "Duplicate game ID shouldn't overwrite the existing game.");
    }

    @Test
    public void gameExistsWorks() {
        DataGame game = new DataGame(2, "white", "black", "TestGame", new ChessGame());
        gameDAO.makeGame(game);

        assertTrue(gameDAO.gameExists(2), "Game with ID 2 should exist.");
    }

    @Test
    public void gameExistsFails() {
        assertFalse(gameDAO.gameExists(999), "Game with non-existent ID should not exist.");
    }

    @Test
    public void getGameWorks() {
        DataGame game = new DataGame(3, "whiteUser", "blackUser", "FunGame", new ChessGame());
        gameDAO.makeGame(game);
        DataGame fetchedGame = null;
        try {
            fetchedGame = gameDAO.getGame(3);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(fetchedGame, "game should not be null.");
        assertEquals("FunGame", fetchedGame.gameName(), "Game name should match.");
    }

    @Test
    public void getGameFails() {
        DataGame fetchedGame = null;
        try {
            fetchedGame = gameDAO.getGame(999);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNull(fetchedGame, "Fetching a non-existent game should return null.");
    }

    @Test
    public void gamesListWorks() {
        gameDAO.makeGame(new DataGame(1, "white1", "black1", "Game1", new ChessGame()));
        gameDAO.makeGame(new DataGame(2, "white2", "black2", "Game2", new ChessGame()));
        HashSet<DataGame> games = gameDAO.gamesList();
        assertEquals(2, games.size(), "There should be 2 games.");
    }

    @Test
    public void gamesListFails() {
        HashSet<DataGame> games = gameDAO.gamesList();
        assertNotNull(games, "Games list should not be null.");
        assertTrue(games.isEmpty(), "Games list should be empty when no games are present.");
    }

    @Test
    public void updateGameWorks() {
        DataGame game = new DataGame(4, "whiteUser", "blackUser", "InitialGame", new ChessGame());
        gameDAO.makeGame(game);

        DataGame updatedGame = new DataGame(4, "newWhite", "newBlack", "UpdatedGame", new ChessGame());
        gameDAO.updateGame(updatedGame);

        DataGame fetchedGame = null;
        try {
            fetchedGame = gameDAO.getGame(4);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertEquals("UpdatedGame", fetchedGame.gameName(), "Game name should be updated.");
    }

    @Test
    public void updateGameFailsWithNonExistentGame() {
        DataGame game = new DataGame(5, "nonWhite", "nonBlack", "NonExistent", new ChessGame());
        gameDAO.updateGame(game);

        try {
            assertNull(gameDAO.getGame(5), "Non-existent game should not be updated.");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void clearWorks() {
        gameDAO.makeGame(new DataGame(1, "white1", "black1", "Game1", new ChessGame()));
        gameDAO.makeGame(new DataGame(2, "white2", "black2", "Game2", new ChessGame()));

        gameDAO.clear();
        assertTrue(gameDAO.gamesList().isEmpty(), "All games should be cleared from the database.");
    }

    @Test
    public void clearFails() {
        gameDAO.clear();
        assertTrue(gameDAO.gamesList().isEmpty(), "Clear should work even if no games are present.");
    }
}
