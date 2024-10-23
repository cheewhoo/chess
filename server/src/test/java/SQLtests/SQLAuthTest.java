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
        authDAO.clear();
    }

    @AfterEach
    public void cleanup() {
        authDAO.clear();
    }

    @Test
    public void AddAuthenticationPass() {
        DataAuth auth = new DataAuth("validToken", "testUser");
        assertDoesNotThrow(() -> authDAO.addAuthentication(auth));
        DataAuth retrievedAuth = assertDoesNotThrow(() -> authDAO.getAuthentication("validToken"));
        assertEquals("validToken", retrievedAuth.authToken());
        assertEquals("testUser", retrievedAuth.username());
    }

    @Test
    public void AddAuthenticationFail() throws DataAccessException {
        SQLAuth authDAO = new SQLAuth();
        DataAuth authData = new DataAuth("existingToken", "testUser");

        // Insert the first time to set up for a conflict on the second insertion
        authDAO.addAuthentication(authData);  // This should succeed.

        // Now try to add it again, expecting DataAccessException due to duplicate insertion.
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authDAO.addAuthentication(authData);  // This should fail with an exception.
        });

        // Optional: Verify the exception message or cause to be sure it's as expected.
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
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authDAO.clear()
        );
        assertTrue(exception.getMessage().contains("Failed to clear authentication data"));
    }
}
