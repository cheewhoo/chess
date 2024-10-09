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
public class GameServiceTest {
    static Service_Game gameService;
    static Game_DAO gameDAO;
    static Auth_DAO authDAO;
    static Data_Auth authData;
    @BeforeAll
    static void setupAll() {
        gameDAO = new Mem_Game_DAO();
        authDAO = new Mem_Auth_DAO();
        Servicegame = new Service_Game(gameDAO, authDAO);
        authInfo = new Data_Auth("Username", "authToken");
        authDAO.addAuthentication(authData);
    }
    @BeforeEach
    void setup() {
        gameDAO.clear();
    }
    @Test
    @DisplayName("Creating game worked")
    void createGameTestPositive() throws UnauthorizedException {
        int firstID = Servicegame.makeGame(authData.authenticationToken());
        Assertions.assertTrue(gameDAO.gameExists(firstID));
        int secondID = Servicegame.makeGame(authData.authenticationToken());
        Assertions.assertNotEquals(firstID, secondID);
    }
    @Test
    @DisplayName("Creating game failed")
    void makeGameFailed() throws UnauthorizedException {
        Assertions.assertThrows(UnauthorizedException.class, () -> Servicegame.makeGame("badToken"));
    }
    @Test
    @DisplayName("List games works")
    void ListGamesWorks() throws UnauthorizedException {
        int firstgame = Servicegame.makeGame(authData.authenticationToken());
        int secondgame = Servicegame.makeGame(authData.authenticationToken());
        int thirdgame = Servicegame.makeGame(authData.authenticationToken());
        HashSet<Data_Game> expected = HashSet<>();
        expected.add(new Data_Game(firstgame, null, null, null, null));
        expected.add(new Data_Game(secondgame, null, null, null, null));
        expected.add(new Data_Game(thirdgame, null, null, null, null));
        Assertions.assertEquals(expected, Servicegame.Games_lst(authData.authenticationToken()));
    }
    @Test
    @DisplayName("List games failed")
    void listGamesfailed() {
        Assertions.assertThrows(UnauthorizedException.class, () -> Servicegame.Games_lst("badToken"));
    }
    @Test
    @DisplayName("Join Ggme worked")
    void joinGameworks() throws UnauthorizedException, BadRequestException, DataAccessException {
        int gameID = Servicegame.makeGame(authData.authenticationToken());
        Servicegame.joinGame(authData.authenticationToken(), gameID, "WHITE");
        Data_Game expectedGame = new Data_Game(gameID, authData.username(), null, null, null);
        Assertions.assertEquals(expectedGame, gameDAO.getGame(gameID));
    }
    @Test
    @DisplayName("Join game failed")
    void joinGameTestNegative() throws UnauthorizedException {
        int gameID = Servicegame.makeGame(authData.authenticationToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> Servicegame.joinGame("badToken", gameID, "WHITE"));
        Assertions.assertThrows(BadRequestException.class, () -> Servicegame.joinGame(authData.authenticationToken(), 11111, "WHITE"));
        Assertions.assertThrows(BadRequestException.class, () -> Servicegame.joinGame(authData.authenticationToken(), gameID, "INVALID"));
    }
    @Test
    @DisplayName("Cleared DB")
    void clearDBworks() throws UnauthorizedException {
        Servicegame.makeGame(authData.authenticationToken());
        Servicegame.clear();
        Assertions.assertEquals(gameDAO.Games_lst(), HashSet<>());
    }
    @Test
    @DisplayName("Clearing DB failed")
    void clearDBfail() throws UnauthorizedException {
        Servicegame.makeGame(authData.authenticationToken());
        HashSet<Data_Game> gameList = gameDAO.Games_lst();
        Servicegame.clear();
        Assertions.assertNotEquals(gameDAO.Games_lst(), gameList);
        Assertions.assertDoesNotThrow(() -> Servicegame.clear());
    }
}