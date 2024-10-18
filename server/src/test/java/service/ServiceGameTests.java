package service;
import dataaccess.*;
import model.Data_Auth;
import model.Data_Game;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class ServiceGameTests{

    private static Service_Game servicegame;
    private static Mem_Game_DAO memgame;
    private static Mem_Auth_DAO memauth;
    private static Data_Auth authenticate;

    @BeforeAll
    static void setup() {
        memgame = new Mem_Game_DAO();
        memauth = new Mem_Auth_DAO();
        servicegame = new Service_Game(memgame, memauth);

        authenticate = new Data_Auth("Username", "authToken");
        memauth.addAuthentication(authenticate);
    }
    @BeforeEach
    void reset() {
        memgame.clear();
    }

    @Test
    void makeGameworks() throws DataAccessException, UnauthorizedException {
        int firstID = servicegame.makeGame(authenticate.authToken());
        Assertions.assertTrue(memgame.gameExists(firstID));
        int secondID = servicegame.makeGame(authenticate.authToken());
        Assertions.assertNotEquals(firstID, secondID);
    }
    @Test
    void makeGameFailed() {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            try {
                servicegame.makeGame("faulty token");
            } catch (DataAccessException e) {
                // Wrap DataAccessException into a RuntimeException to ensure it doesn't break the test.
                throw new RuntimeException("Unexpected DataAccessException", e);
            }
        });
    }


    @Test
    void listGamesworks() throws DataAccessException, UnauthorizedException {
        String authToken = authenticate.authToken();
        int firstGame = servicegame.makeGame(authToken);
        int secondGame = servicegame.makeGame(authToken);
        HashSet<Data_Game> expectedGameList = new LinkedHashSet<>();
        expectedGameList.add(new Data_Game(firstGame, null, null, null, null));
        expectedGameList.add(new Data_Game(secondGame, null, null, null, null));
        HashSet<Data_Game> actualGameList = servicegame.Games_lst(authToken);
        Assertions.assertEquals(expectedGameList, actualGameList);
    }

    @Test
    void listGamesfailed() {
        Assertions.assertThrows(UnauthorizedException.class, () -> servicegame.Games_lst("wrong token"));
    }
    @Test
    void JoinGameWorks() throws UnauthorizedException, DataAccessException {
        String authToken = authenticate.authToken();
        String username = authenticate.username();
        int gameID = servicegame.makeGame(authToken);
        servicegame.joinGame(authToken, gameID, "WHITE");

        Data_Game expectedGame = new Data_Game(gameID, username, null, null, null);
        Data_Game actualGame = memgame.getGame(gameID);
        Assertions.assertEquals(expectedGame, actualGame);
    }

    @Test
    void joinGameTestNegative() throws DataAccessException, UnauthorizedException {
        int gameID = servicegame.makeGame(authenticate.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> servicegame.joinGame("wrong token", gameID, "WHITE"));
       }
    @Test
    void clearDBworks() throws DataAccessException, UnauthorizedException {
        Game_DAO gameDAO = new Mem_Game_DAO();
        Auth_DAO authDAO = new Mem_Auth_DAO();
        Data_Auth authData = new Data_Auth("Username", "authToken");
        authDAO.addAuthentication(authData);
        servicegame.makeGame(authData.authToken());
        servicegame.clearGames();
        Assertions.assertEquals(new HashSet<>(), gameDAO.Games_lst());
    }
    @Test
    void clearDBfail() throws DataAccessException, UnauthorizedException {
        servicegame.makeGame(authenticate.authToken());
        HashSet<Data_Game> gameList = memgame.Games_lst();
        servicegame.clearGames();
        Assertions.assertNotEquals(memgame.Games_lst(), gameList);
        Assertions.assertDoesNotThrow(() -> servicegame.clearGames());
    }
}