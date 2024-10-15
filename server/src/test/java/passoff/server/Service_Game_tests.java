package passoff.server;
import dataaccess.*;
import model.Data_Auth;
import model.Data_Game;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import service.Service_Game;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Service_Game_tests{

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
    void makeGameworks() throws DataAccessException{
        int firstID = servicegame.makeGame(authenticate.authenticationToken());
        Assertions.assertTrue(memgame.gameExists(firstID));
        int secondID = servicegame.makeGame(authenticate.authenticationToken());
        Assertions.assertNotEquals(firstID, secondID);
    }
    @Test
    void makeGameFailed() {
        Assertions.assertThrows(UnauthorizedException.class, () -> servicegame.makeGame("faulty token"));
    }
    @Test
    void listGamesworks() throws DataAccessException, UnauthorizedException {
        String authToken = authenticate.authenticationToken();
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
        String authToken = authenticate.authenticationToken();
        String username = authenticate.username();
        int gameID = servicegame.makeGame(authToken);
        servicegame.joinGame(authToken, gameID, "WHITE");

        Data_Game expectedGame = new Data_Game(gameID, username, null, null, null);
        Data_Game actualGame = memgame.getGame(gameID);
        Assertions.assertEquals(expectedGame, actualGame);
    }

    @Test
    void joinGameTestNegative() throws DataAccessException {
        int gameID = servicegame.makeGame(authenticate.authenticationToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> servicegame.joinGame("wrong token", gameID, "WHITE"));
       }
    @Test
    void clearDBworks() throws DataAccessException{
        Game_DAO gameDAO = new Mem_Game_DAO();
        Auth_DAO authDAO = new Mem_Auth_DAO();
        Data_Auth authData = new Data_Auth("Username", "authToken");
        authDAO.addAuthentication(authData);
        servicegame.makeGame(authData.authenticationToken());
        servicegame.clearGames();
        Assertions.assertEquals(new HashSet<>(), gameDAO.Games_lst());
    }
    @Test
    void clearDBfail() throws DataAccessException {
        servicegame.makeGame(authenticate.authenticationToken());
        HashSet<Data_Game> gameList = memgame.Games_lst();
        servicegame.clearGames();
        Assertions.assertNotEquals(memgame.Games_lst(), gameList);
        Assertions.assertDoesNotThrow(() -> servicegame.clearGames());
    }
}