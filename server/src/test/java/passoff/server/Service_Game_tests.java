package passoffTests.serverTests;
import dataaccess.*;
import dataaccess.Mem_Auth_DAO;
import dataaccess.Mem_Game_DAO;
import dataaccess.Game_DAO;
import dataaccess.Auth_DAO;
import model.Data_Auth;
import model.Data_Game;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import service.Service_Game;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class serviceGametest {

    @BeforeAll
    void setup() {
        servicegame = new Service_Game(Mem_Game_DAO, Mem_Auth_DAO);
        authenticate = new Data_Auth("Username", "authToken");
        Mem_Auth_DAO.addAuthentication(authData);
    }
    @BeforeEach
    void setup() {
        Mem_Game_DAO.clear();
    }

    @Test
    @DisplayName("Creating game worked")
    void makeGameworks() throws UnauthorizedException {
        int firstID = servicegame.makeGame(authenticate.authenticationToken());
        Assertions.assertTrue(Mem_Game_DAO.gameExists(firstID));
        int secondID = servicegame.makeGame(authenticate.authenticationToken());
        Assertions.assertNotEquals(firstID, secondID);
    }
    @Test
    @DisplayName("Creating game failed")
    void makeGameFailed() throws UnauthorizedException {
        Assertions.assertThrows(UnauthorizedException.class, () -> servicegame.makeGame("invalid Token"));
    }
    @Test
    @DisplayName("List games works")
    void ListGamesWorks() throws UnauthorizedException {
        int firstgame = servicegame.makeGame(authenticate.authenticationToken());
        int secondgame = servicegame.makeGame(authenticate.authenticationToken());
        int thirdgame = servicegame.makeGame(authenticate.authenticationToken());
        HashSet<Data_Game> expectedGamelst = new LinkedHashSet<>();
        expectedGameslst.add(new Data_Game(firstgame, null, null, null, null));
        expectedGameslst.add(new Data_Game(secondgame, null, null, null, null));
        expectedGameslst.add(new Data_Game(thirdgame, null, null, null, null));
        Assertions.assertEquals(expectedGameslst, servicegame.Games_lst(authInfo.authenticationToken()));
    }
    @Test
    @DisplayName("List games failed")
    void listGamesfailed() {
        Assertions.assertThrows(UnauthorizedException.class, () -> servicegame.Games_lst("wrong Token"));
    }
    @Test
    @DisplayName("Join Ggme worked")
    void joinGameworks() throws UnauthorizedException, DataAccessException {
        int gameID = servicegame.makeGame(authenticate.authenticationToken());
        servicegame.joinGame(authenticate.authenticationToken(), gameID, "WHITE");
        Data_Game expectedGame = new Data_Game(gameID, authenticate.username(), null, null, null);
        Assertions.assertEquals(expectedGame, memgameDAO.getGame(gameID));
    }
    @Test
    @DisplayName("Join game failed")
    void joinGameTestNegative() throws UnauthorizedException {
        int gameID = servicegame.makeGame(authenticate.authenticationToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> servicegame.joinGame("badToken", gameID, "WHITE"));
       }
    @Test
    @DisplayName("Cleared DB")
    void clearDBworks() throws UnauthorizedException {
        Game_DAO gameDAO = new Mem_Game_DAO();
        Auth_DAO authDAO = new Mem_Auth_DAO();
        Data_Auth authData = new Data_Auth("Username", "authToken");
        authDAO.addAuthentication(authData);
        servicegame.makeGame(authData.authenticationToken());
        servicegame.clear();
        Assertions.assertEquals(gameDAO.Games_lst(), new HashSet<>());
    }
    @Test
    @DisplayName("Clearing DB failed")
    void clearDBfail() throws UnauthorizedException {
        servicegame.makeGame(authenticate.authenticationToken());
        HashSet<Data_Game> gameList = Mem_Game_DAO.Games_lst();
        servicegame.clear();
        Assertions.assertNotEquals(Mem_Game_DAO.Games_lst(), gameList);
        Assertions.assertDoesNotThrow(() -> servicegame.clear());
    }
}