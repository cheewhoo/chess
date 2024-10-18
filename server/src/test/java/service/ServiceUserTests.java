package service;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;
import service.Service_User;

public class ServiceUserTests{

    private static Service_User serviceuser;
    private static Mem_User_DAO memuser;
    private static Mem_Auth_DAO memauth;
    private Data_User user;

    @BeforeAll
    static void starter() {
        memuser = new Mem_User_DAO();
        memauth = new Mem_Auth_DAO();
        serviceuser = new Service_User(memuser, memauth);
    }
    @BeforeEach
    void setup() {
        memuser.clear();
        memauth.clear();
        user = new Data_User("Username", "password", "email");
    }
    @Test
    void makeuserworks() throws DataAccessException, UnauthorizedException, UserAlreadyExistsException {
        Data_Auth authenticateResult = serviceuser.makeUser(user);
        Assertions.assertEquals(memauth.getAuthentication(authenticateResult.authToken()), authenticateResult);
    }
    @Test
    void makeuserfails() throws UnauthorizedException, DataAccessException, UserAlreadyExistsException {
        serviceuser.makeUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> serviceuser.makeUser(user));
    }
    @Test
    void loginworks() throws UnauthorizedException, DataAccessException,UserAlreadyExistsException {
        serviceuser.makeUser(user);
        Data_Auth authData = serviceuser.loginUser(user);
        Assertions.assertEquals(memauth.getAuthentication(authData.authToken()), authData);
    }
    @Test
    void loginfails() throws UnauthorizedException, DataAccessException, UserAlreadyExistsException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            serviceuser.loginUser(user);
        });
        serviceuser.makeUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> {
            serviceuser.loginUser(new Data_User(user.username(), "wrong password", user.email()));
        });
    }
    @Test
    void logoutworks() throws UnauthorizedException, DataAccessException, UserAlreadyExistsException {
        Data_Auth authenticate = serviceuser.makeUser(user);
        serviceuser.logoutUser(authenticate.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> memauth.getAuthentication(authenticate.authToken()));
    }
    @Test
    void logoutfails() throws DataAccessException, UnauthorizedException, UserAlreadyExistsException {
        Data_Auth auth = serviceuser.makeUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> serviceuser.logoutUser("failed Auth"));
    }
    @Test
    void clearworks() throws DataAccessException, UnauthorizedException, UserAlreadyExistsException {
        Data_Auth authenticate = serviceuser.makeUser(user);
        serviceuser.clearUsers();
        Assertions.assertThrows(DataAccessException.class, () -> memuser.getUser(user.username()));
        Assertions.assertThrows(DataAccessException.class, () -> memauth.getAuthentication(authenticate.authToken()));
    }
    @Test
    void clearfails() {
        Assertions.assertDoesNotThrow(() -> serviceuser.clearUsers());
    }
}