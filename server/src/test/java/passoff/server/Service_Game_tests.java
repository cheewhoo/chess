package passoffTests.serverTests;
import dataAccess.*;
import dataaccess.Mem_Auth_DAO;
import dataaccess.Mem_Game_DAO;
import model.Data_Auth;
import model.Data_Game;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import service.Service_Game;
import java.util.HashSet;
public class GameServiceTest {
    static Service_Game gameService;
    static Game_DAO gameDAO;
    static Auth_DAO authDAO;
    static Data_Auth authData;
    @BeforeAll
    static void init() {
        gameDAO = new Mem_Game_DAO();
        authDAO = new Mem_Auth_DAO();
        gameService = new Service_Game(gameDAO, authDAO);
        authData = new Data_Auth("Username", "authToken");
        authDAO.addAuthentication(authData);
    }
    @BeforeEach
    void setup() {
        gameDAO.clear();
    }
    @Test
    @DisplayName("Create Valid Game")
    void createGameTestPositive() throws UnauthorizedException {
        int gameID1 = gameService.makeGame(authData.authenticationToken());
        Assertions.assertTrue(gameDAO.gameExists(gameID1));
        int gameID2 = gameService.makeGame(authData.authenticationToken());
        Assertions.assertNotEquals(gameID1, gameID2);
    }
    @Test
    @DisplayName("Create Invalid Game")
    void createGameTestNegative() throws UnauthorizedException {
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.makeGame("badToken"));
    }
    @Test
    @DisplayName("Proper List Games")
    void listGamesTestPositive() throws UnauthorizedException {
        int gameID1 = gameService.makeGame(authData.authenticationToken());
        int gameID2 = gameService.makeGame(authData.authenticationToken());
        int gameID3 = gameService.makeGame(authData.authenticationToken());
        HashSet<Data_Game> expected = HashSet.newHashSet(8);
        expected.add(new Data_Game(gameID1, null, null, null, null));
        expected.add(new Data_Game(gameID2, null, null, null, null));
        expected.add(new Data_Game(gameID3, null, null, null, null));
        Assertions.assertEquals(expected, gameService.Games_lst(authData.authenticationToken()));
    }
    @Test
    @DisplayName("Improper List Games")
    void listGamesTestNegative() {
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.Games_lst("badToken"));
    }
    @Test
    @DisplayName("Proper Join Game")
    void joinGameTestPositive() throws UnauthorizedException, BadRequestException, DataAccessException {
        int gameID = gameService.makeGame(authData.authenticationToken());
        gameService.joinGame(authData.authenticationToken(), gameID, "WHITE");
        Data_Game expectedGameData = new Data_Game(gameID, authData.username(), null, null, null);
        Assertions.assertEquals(expectedGameData, gameDAO.getGame(gameID));
    }
    @Test
    @DisplayName("Improper Join Game")
    void joinGameTestNegative() throws UnauthorizedException {
        int gameID = gameService.makeGame(authData.authenticationToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.joinGame("badToken", gameID, "WHITE"));
        Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame(authData.authenticationToken(), 11111, "WHITE"));
        Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame(authData.authenticationToken(), gameID, "INVALID"));
    }
    @Test
    @DisplayName("Proper Clear DB")
    void clearTestPositive() throws UnauthorizedException {
        gameService.makeGame(authData.authenticationToken());
        gameService.clear();
        Assertions.assertEquals(gameDAO.Games_lst(), HashSet.newHashSet(16));
    }
    @Test
    @DisplayName("Improper Clear DB")
    void clearTestNegative() throws UnauthorizedException {
        gameService.makeGame(authData.authenticationToken());
        HashSet<Data_Game> gameList = gameDAO.Games_lst();
        gameService.clear();
        Assertions.assertNotEquals(gameDAO.Games_lst(), gameList);
        Assertions.assertDoesNotThrow(() -> gameService.clear());
    }
}