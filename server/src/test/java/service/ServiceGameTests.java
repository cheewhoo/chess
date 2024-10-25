package service;
import dataaccess.*;
import model.DataAuth;
import model.DataGame;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class ServiceGameTests{

    private static ServiceGame servicegame;
    private static MemGameDAO memgame;
    private static MemAuthDAO memauth;
    private static DataAuth authenticate;

    @BeforeAll
    static void setup() {
        memgame = new MemGameDAO();
        memauth = new MemAuthDAO();
        servicegame = new ServiceGame(memgame, memauth);

        authenticate = new DataAuth("Username", "authToken");
        memauth.addAuthentication(authenticate);
    }
    @BeforeEach
    void reset() {
        memgame.clear();
    }

    @Test
    void makeGameworks() throws DataAccessException, UnauthorizedException {
        int firstID = servicegame.makeGame(authenticate.authToken(), "gamename");
        Assertions.assertTrue(memgame.gameExists(firstID));
        int secondID = servicegame.makeGame(authenticate.authToken(), "gamename");
        Assertions.assertNotEquals(firstID, secondID);
    }
    @Test
    void makeGameFailed() {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            try {
                servicegame.makeGame("faulty token", "gamename");
            } catch (DataAccessException e) {
                // Wrap DataAccessException into a RuntimeException to ensure it doesn't break the test.
                throw new RuntimeException("Unexpected DataAccessException", e);
            }
        });
    }


    @Test
    void listGamesworks() throws DataAccessException, UnauthorizedException {
        String authToken = authenticate.authToken();
        int firstGame = servicegame.makeGame(authToken, "gamename");
        int secondGame = servicegame.makeGame(authToken, "gamename");
        HashSet<DataGame> expectedGameList = new LinkedHashSet<>();
        expectedGameList.add(new DataGame(firstGame, "Username", null, "gamename", null));
        expectedGameList.add(new DataGame(secondGame, "Username", null, "gamename", null));
        HashSet<DataGame> actualGameList = servicegame.gamesList(authToken);
        Assertions.assertEquals(expectedGameList, actualGameList);
    }

    @Test
    void listGamesfailed() {
        Assertions.assertThrows(UnauthorizedException.class, () -> servicegame.gamesList("wrong token"));
    }
    @Test
    void joinGameWorks() throws UnauthorizedException, DataAccessException {
        String authToken = authenticate.authToken();
        String username = authenticate.username();
        int gameID = servicegame.makeGame(authToken, "gamename");
        servicegame.joinGame(authToken, gameID, "WHITE");

        DataGame expectedGame = new DataGame(gameID, username, null, "gamename", null);
        DataGame actualGame = memgame.getGame(gameID);
        Assertions.assertEquals(expectedGame, actualGame);
    }

    @Test
    void joinGameTestNegative() throws DataAccessException, UnauthorizedException {
        int gameID = servicegame.makeGame(authenticate.authToken(), "gamename");
        Assertions.assertThrows(UnauthorizedException.class, () -> servicegame.joinGame("wrong token", gameID, "WHITE"));
       }
    @Test
    void clearDBworks() throws DataAccessException, UnauthorizedException {
        GameDAO gameDAO = new MemGameDAO();
        AuthDAO authDAO = new MemAuthDAO();
        DataAuth authData = new DataAuth("Username", "authToken");
        authDAO.addAuthentication(authData);
        servicegame.makeGame(authData.authToken(), "gamename");
        servicegame.clearGames();
        Assertions.assertEquals(new HashSet<>(), gameDAO.gamesList());
    }
    @Test
    void clearDBfail() throws DataAccessException, UnauthorizedException {
        servicegame.makeGame(authenticate.authToken(), "gamename");
        HashSet<DataGame> gameList = memgame.gamesList();
        servicegame.clearGames();
        Assertions.assertNotEquals(memgame.gamesList(), gameList);
        Assertions.assertDoesNotThrow(() -> servicegame.clearGames());
    }
}