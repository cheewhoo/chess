package service;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;

public class ServiceUserTests{

    private static Service_User serviceuser;
    private static MemUserDAO memuser;
    private static MemAuthDAO memauth;
    private DataUser user;

    @BeforeAll
    static void starter() {
        memuser = new MemUserDAO();
        memauth = new MemAuthDAO();
        serviceuser = new Service_User(memuser, memauth);
    }
    @BeforeEach
    void setup() {
        memuser.clear();
        memauth.clear();
        user = new DataUser("Username", "password", "email");
    }
    @Test
    void makeuserworks() throws DataAccessException, UnauthorizedException, UserAlreadyExistsException {
        DataAuth authenticateResult = serviceuser.makeUser(user);
        Assertions.assertEquals(memauth.getAuthentication(authenticateResult.authToken()), authenticateResult);
    }
    @Test
    void makeuserfails() throws UnauthorizedException, DataAccessException, UserAlreadyExistsException {
        serviceuser.makeUser(user);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> serviceuser.makeUser(user));
    }
    @Test
    void loginworks() throws UnauthorizedException, DataAccessException,UserAlreadyExistsException {
        serviceuser.makeUser(user);
        DataAuth authData = serviceuser.loginUser(user);
        Assertions.assertEquals(memauth.getAuthentication(authData.authToken()), authData);
    }
    @Test
    void loginfails() throws UnauthorizedException, DataAccessException, UserAlreadyExistsException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            serviceuser.loginUser(user);
        });
        serviceuser.makeUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> {
            serviceuser.loginUser(new DataUser(user.username(), "wrong password", user.email()));
        });
    }
    @Test
    void logoutworks() throws UnauthorizedException, DataAccessException, UserAlreadyExistsException {
        DataAuth authenticate = serviceuser.makeUser(user);
        serviceuser.logoutUser(authenticate.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> memauth.getAuthentication(authenticate.authToken()));
    }
    @Test
    void logoutfails() throws DataAccessException, UnauthorizedException, UserAlreadyExistsException {
        DataAuth auth = serviceuser.makeUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> serviceuser.logoutUser("failed Auth"));
    }
    @Test
    void clearworks() throws DataAccessException, UnauthorizedException, UserAlreadyExistsException {
        DataAuth authenticate = serviceuser.makeUser(user);
        serviceuser.clearUsers();
        Assertions.assertThrows(DataAccessException.class, () -> memuser.getUser(user.username()));
        Assertions.assertThrows(DataAccessException.class, () -> memauth.getAuthentication(authenticate.authToken()));
    }
    @Test
    void clearfails() {
        Assertions.assertDoesNotThrow(() -> serviceuser.clearUsers());
    }
}