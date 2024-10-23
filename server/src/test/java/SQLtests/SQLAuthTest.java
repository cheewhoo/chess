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
    public void AddAuthenticationFail() {
        DataAuth auth = new DataAuth("duplicateToken", "user1");
        authDAO.addAuthentication(auth);
        DataAccessException exception = assertThrows(
                DataAccessException.class,
                () -> authDAO.addAuthentication(new DataAuth("duplicateToken", "user2"))
        );

        assertEquals("Could not add authentication: duplicateToken", exception.getMessage());
    }

    @Test
    public void GetAuthenticationPass() {
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
    public void DeleteAuthenticationPass() {
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
    public void ClearPass() {
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
