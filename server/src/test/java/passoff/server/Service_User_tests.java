package passoffTests.serverTests;
import dataAccess.*;
import model.Data_Auth;
import model.Data_User;
import org.junit.jupiter.api.*;
import service.Service_User;

public class UserServiceTest {
    static Service_User userService;
    static User_DAO userDAO;
    static Auth_DAO authDAO;
    static User_Data defaultUser;
    @BeforeAll
    static void init() {
        userDAO = new Mem_User_DAO();
        authDAO = new Mem_Auth_DAO();
        userService = new Service_User(userDAO, authDAO);
    }
    @BeforeEach
    void setup() {
        userDAO.clear();
        authDAO.clear();
        defaultUser = new Data_User("Username", "password", "email");
    }
    @Test
    @DisplayName("Create Valid User")
    void createUserTestPositive() throws BadRequestException, DataAccessException {
        AuthData resultAuth = userService.makeUser(defaultUser);
        Assertions.assertEquals(authDAO.getAuthentication(resultAuth.authenticationToken()), resultAuth);
    }
    @Test
    @DisplayName("Create Invalid User")
    void createUserTestNegative() throws BadRequestException {
        userService.makeUser(defaultUser);
        Assertions.assertThrows(BadRequestException.class, () -> userService.makeUser(defaultUser));
    }
    @Test
    @DisplayName("Proper Login User")
    void loginUserTestPositive() throws BadRequestException, UnauthorizedException, DataAccessException {
        userService.makeUser(defaultUser);
        Data_Auth authData = userService.loginUser(defaultUser);
        Assertions.assertEquals(authDAO.getAuthentication(authData.authenticationToken()), authData);
    }
    @Test
    @DisplayName("Improper Login User")
    void loginUserTestNegative() throws BadRequestException {
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(defaultUser));
        userService.makeUser(defaultUser);
        UserData badPassUser = new Data_User(defaultUser.username(), "wrongPass", defaultUser.email());
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(badPassUser));
    }
    @Test
    @DisplayName("Proper Logout User")
    void logoutUserTestPositive() throws BadRequestException, UnauthorizedException {
        AuthData auth = Service_User.createUser(defaultUser);
        userService.logoutUser(auth.authenticationToken());
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuthentication(auth.authenticationToken()));
    }
    @Test
    @DisplayName("Improper Logout User")
    void logoutUserTestNegative() throws BadRequestException {
        Data_Auth auth = userService.makeUser(defaultUser);
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logoutUser("badAuthToken"));
    }
    @Test
    @DisplayName("Proper Clear DB")
    void clearTestPositive() throws BadRequestException {
        Data_Auth auth = userService.makeUser(defaultUser);
        userService.clear();
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.getUser(defaultUser.username()));
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuthentication(auth.authenticationToken()));
    }
    @Test
    @DisplayName("Improper Clear DB")
    void clearTestNegative() throws BadRequestException {
        Assertions.assertDoesNotThrow(() -> userService.clear());
    }
}