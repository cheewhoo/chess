package passoffTests.serverTests;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;
import service.Service_User;

public class serviceUserTest {
    @BeforeAll
    void starter() {
        serviceuser = new Service_User(new Mem_User_DAO(), new Mem_Auth_DAO());
    }
    @BeforeEach
    void setup() {
        Mem_User_DAO.clear();
        Mem_Auth_DAO.clear();
        user = new Data_User("Username", "password");
    }
    @Test
    void makeuserworks() throws DataAccessException {
        AuthData authenticateResult = serviceuser.makeUser(user);
        Assertions.assertEquals(Mem_Auth_DAO.getAuthentication(authenticateResult.authenticationToken()), authenticateResult);
    }
    @Test
    void makeuserfails() throws UnauthorizedException {
        serviceuser.makeUser(user);
        Assertions.assertThrows(UnauthorizedException.class, () -> serviceuser.makeUser(user));
    }
    @Test
    void loginworks() throws UnauthorizedException, DataAccessException {
        serviceuser.makeUser(user);
        Data_Auth authData = serviceuser.loginUser(user);
        Assertions.assertEquals(Mem_Auth_DAO.getAuthentication(authData.authenticationToken()), authData);
    }
    @Test
    void loginfails() throws UnauthorizedException, DataAccessException {
        Assertions.assertThrows(UnauthorizedException.class, () -> serviceuser.loginUser(user));
        serviceuser.makeUser(user);
        Assertions.assertThrows(UnauthorizedException.class, () -> serviceuser.loginUser(new Data_User(user.username(), "wrongPass")));
    }
    @Test
    void logoutworks() throws UnauthorizedException {
        AuthData authenticate = Service_User.makeUser(user);
        serviceuser.logoutUser(authenticate.authenticationToken());
        Assertions.assertThrows(DataAccessException.class, () -> Mem_Auth_DAO.getAuthentication(authenticate.authenticationToken()));
    }
    @Test
    void logoutfails() throws UnauthorizedException {
        Data_Auth auth = serviceuser.makeUser(user);
        Assertions.assertThrows(UnauthorizedException.class, () -> serviceuser.logoutUser("failed Auth"));
    }
    @Test
    void clearworks() throws BadRequestException {
        Data_Auth authenticate = serviceuser.makeUser(user);
        serviceuser.clear();
        Assertions.assertThrows(DataAccessException.class, () -> Mem_User_DAO.getUser(user.username()));
        Assertions.assertThrows(DataAccessException.class, () -> Mem_Auth_DAO.getAuthentication(authenticate.authenticationToken()));
    }
    @Test
    void clearfails() {
        Assertions.assertDoesNotThrow(() -> serviceuser.clear());
    }
}