package client;

import ui.*;
import org.junit.jupiter.api.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonalFacadeTest {
    private static final String BASE_URL = "http://localhost:8080";
    private ServerFacade serverFacade;

    @BeforeEach
    public void setUp() {
        serverFacade = new ServerFacade(BASE_URL);
    }

    @Test
    @Order(1)
    @DisplayName("Register: Successful Registration")
    public void testRegister_Success() {
        Map<String, Object> response = serverFacade.register("newUser", "validPassword", "email@example.com");
        System.out.println("Response: " + response); // Debug line to check response structure
        assertNotNull(response);
        assertTrue(response.containsKey("success") && (boolean) response.get("success"),
                "Expected response to contain success:true, but got: " + response);
    }


    @Test
    @Order(2)
    @DisplayName("Register: User Already Exists")
    public void testRegister_UserAlreadyExists() {
        serverFacade.register("existingUser", "validPassword", "email@example.com"); // Register once
        Map<String, Object> response = serverFacade.register("existingUser", "anotherPassword", "email@example.com");
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("User already exists", response.get("Error"));
    }

    @Test
    @Order(3)
    @DisplayName("Login: Successful Login")
    public void testLogin_Success() {
        serverFacade.register("testUser", "password123", "test@example.com");
        Map<String, Object> response = serverFacade.login("testUser", "password123");
        assertNotNull(response);
        assertTrue(response.containsKey("success") && (boolean) response.get("success"));
    }

    @Test
    @Order(4)
    @DisplayName("Login: Incorrect Password")
    public void testLogin_IncorrectPassword() {
        serverFacade.register("testUser2", "password123", "test2@example.com");
        Map<String, Object> response = serverFacade.login("testUser2", "wrongPassword");
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Invalid credentials", response.get("Error"));
    }

    @Test
    @Order(5)
    @DisplayName("Logout: Successful Logout")
    public void testLogout_Success() {
        serverFacade.register("logoutUser", "password123", "logout@example.com");
        serverFacade.login("logoutUser", "password123");
        Map<String, Object> response = serverFacade.logout();
        assertNotNull(response);
        assertTrue(response.containsKey("success") && (boolean) response.get("success"));
    }

    @Test
    @Order(6)
    @DisplayName("Logout: No Auth Token Provided")
    public void testLogout_NoAuthToken() {
        serverFacade.setAuthToken(null); // Clear auth token
        Map<String, Object> response = serverFacade.logout();
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Unauthorized", response.get("Error"));
    }

    @Test
    @Order(7)
    @DisplayName("Create Game: Successful Game Creation")
    public void testCreateGame_Success() {
        serverFacade.register("gameCreator", "password123", "game@example.com");
        serverFacade.login("gameCreator", "password123");
        Map<String, Object> response = serverFacade.createGame("Chess Game 1");
        assertNotNull(response);
        assertTrue(response.containsKey("success") && (boolean) response.get("success"));
    }

    @Test
    @Order(8)
    @DisplayName("Create Game: No Auth Token Provided")
    public void testCreateGame_NoAuthToken() {
        serverFacade.setAuthToken(null); // Clear auth token
        Map<String, Object> response = serverFacade.createGame("Chess Game 2");
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Unauthorized", response.get("Error"));
    }

    @Test
    @Order(9)
    @DisplayName("List Games: Successfully Retrieve List")
    public void testListGames_Success() {
        serverFacade.register("listUser", "password123", "list@example.com");
        serverFacade.login("listUser", "password123");
        serverFacade.createGame("Game 1");
        serverFacade.createGame("Game 2");

        Map<String, Object> response = serverFacade.listGames();
        assertNotNull(response);
        assertTrue(response.containsKey("games"));
    }

    @Test
    @Order(10)
    @DisplayName("List Games: No Auth Token Provided")
    public void testListGames_NoAuthToken() {
        serverFacade.setAuthToken(null); // Clear auth token
        Map<String, Object> response = serverFacade.listGames();
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Unauthorized", response.get("Error"));
    }
}
