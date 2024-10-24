package SQLtests;
import dataaccess.*;
import model.DataAuth;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthTest {
    private SQLAuth authDAO;

    @BeforeEach
    public void setup() {
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
    public void AddAuthenticationPass() {
        SQLAuth authDAO = new SQLAuth();
        DataAuth authData = new DataAuth("validToken", "testUser");

        // First, add the authentication (should not throw an exception)
        assertDoesNotThrow(() -> authDAO.addAuthentication(authData));

        // Now, try to retrieve it to verify it was added (if your method does this)
        DataAuth retrievedAuth = assertDoesNotThrow(() -> authDAO.getAuthentication("validToken"));
        assertNotNull(retrievedAuth); // Ensure something is retrieved
        assertEquals("testUser", retrievedAuth.username()); // Verify the user is correct
    }


    @Test
    public void AddAuthenticationFail() throws DataAccessException {
        SQLAuth authDAO = new SQLAuth();
        DataAuth authData = new DataAuth("existingToken", "testUser");
        authDAO.addAuthentication(authData);
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authDAO.addAuthentication(authData);
        });
        assertTrue(exception.getMessage().contains("Could not add authentication"));
    }


    @Test
    public void GetAuthenticationPass() throws DataAccessException {
        DataAuth auth = new DataAuth("passToken", "passUser");
        authDAO.addAuthentication(auth);
        DataAuth retrievedAuth = assertDoesNotThrow(() -> authDAO.getAuthentication("passToken"));
        assertEquals("passToken", retrievedAuth.authToken());
        assertEquals("passUser", retrievedAuth.username());
    }


    @Test
    public void GetAuthenticationFail() {
        DataAccessException exception = assertThrows(
                DataAccessException.class,
                () -> authDAO.getAuthentication("invalidToken")
        );

        assertEquals("No authentication found for token: invalidToken", exception.getMessage());
    }

    @Test
    public void DeleteAuthenticationPass() throws DataAccessException {
        DataAuth auth = new DataAuth("deleteToken", "testUser");
        authDAO.addAuthentication(auth);
        assertDoesNotThrow(() -> authDAO.deleteAuthentication("deleteToken"));
        assertThrows(DataAccessException.class, () -> authDAO.getAuthentication("deleteToken"));
    }

    @Test
    public void DeleteAuthenticationFail() {
        assertDoesNotThrow(() -> authDAO.deleteAuthentication("nonExistentToken"));
    }



    @Test
    public void ClearPass() throws DataAccessException {
        authDAO.addAuthentication(new DataAuth("token1", "user1"));
        authDAO.addAuthentication(new DataAuth("token2", "user2"));
        assertDoesNotThrow(() -> authDAO.clear());
        assertThrows(DataAccessException.class, () -> authDAO.getAuthentication("token1"));
        assertThrows(DataAccessException.class, () -> authDAO.getAuthentication("token2"));
    }

    @Test
    public void ClearFail() {
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement("DROP TABLE IF EXISTS auth")) {
            statement.execute();
        } catch (Exception e) {
            fail("Setup for failure simulation failed: " + e.getMessage());
        }
        assertDoesNotThrow(() -> authDAO.clear());
    }

}
