package dataaccess.sqltests;
import dataaccess.*;
import model.DataAuth;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTest {
    private SQLAuth authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SQLAuth();
        authDAO.setupAuthTable();
        try {
            authDAO.addAuthentication(new DataAuth("username", "validToken"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void cleanup() {
        authDAO.clear();
    }

    @Test
    public void addAuthenticationPass() throws DataAccessException {
        SQLAuth authDAO = new SQLAuth();
        DataAuth authData = new DataAuth("testUser", "validToken");
//        assertDoesNotThrow(() -> authDAO.addAuthentication(authData));
        DataAuth retrievedAuth = assertDoesNotThrow(() -> authDAO.getAuthentication("validToken"));
        assertNotNull(retrievedAuth); // Ensure something is retrieved
        assertEquals(retrievedAuth.username(),"username");
    }


    @Test
    public void addAuthenticationFail() throws DataAccessException {
        SQLAuth authDAO = new SQLAuth();
        DataAuth authData = new DataAuth("existingToken", "testUser");
        authDAO.addAuthentication(authData);
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authDAO.addAuthentication(authData);
        });
        assertTrue(exception.getMessage().contains("Could not add authentication"));
    }


    @Test
    public void getAuthenticationPass() throws DataAccessException {
        DataAuth auth = new DataAuth("passUser", "passToken");
        authDAO.addAuthentication(auth);
        DataAuth retrievedAuth = assertDoesNotThrow(() -> authDAO.getAuthentication("passToken"));
        assertEquals("passToken", retrievedAuth.authToken());
        assertEquals("passUser", retrievedAuth.username());
    }


    @Test
    public void getAuthenticationFail() {
        DataAccessException exception = assertThrows(
                DataAccessException.class,
                () -> authDAO.getAuthentication("invalidToken")
        );

        assertEquals("No authentication found for token: invalidToken", exception.getMessage());
    }

    @Test
    public void deleteAuthenticationPass() throws DataAccessException {
        DataAuth auth = new DataAuth("deleteToken", "testUser");
        authDAO.addAuthentication(auth);
        assertDoesNotThrow(() -> authDAO.deleteAuthentication("deleteToken"));
        assertThrows(DataAccessException.class, () -> authDAO.getAuthentication("deleteToken"));
    }

    @Test
    public void deleteAuthenticationFail() {
        assertDoesNotThrow(() -> authDAO.deleteAuthentication("nonExistentToken"));
    }



    @Test
    public void clearPass() throws DataAccessException {
        authDAO.addAuthentication(new DataAuth("token1", "user1"));
        authDAO.addAuthentication(new DataAuth("token2", "user2"));
        assertDoesNotThrow(() -> authDAO.clear());
        assertThrows(DataAccessException.class, () -> authDAO.getAuthentication("token1"));
        assertThrows(DataAccessException.class, () -> authDAO.getAuthentication("token2"));
    }

    @Test
    public void clearFail() {
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement("DROP TABLE IF EXISTS auth")) {
            statement.execute();
        } catch (Exception e) {
            fail("Setup for failure simulation failed: " + e.getMessage());
        }
        assertDoesNotThrow(() -> authDAO.clear());
    }

}
