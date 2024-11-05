package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(1)
    public void testRegister_Success() {
        Map<String, Object> response = serverFacade.register("newUser", "validPassword", "email@example.com");
        System.out.println("Response: " + response);
        assertNotNull(response);
        assertTrue(response.containsKey("success") && (boolean) response.get("success"),
                "Expected response to contain success:true, but got: " + response);
    }

    @Test
    @Order(2)
    public void testRegister_UserAlreadyExists() {
        serverFacade.register("existingUser", "validPassword", "email@example.com");
        Map<String, Object> response = serverFacade.register("existingUser", "anotherPassword", "email@example.com");
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("User already exists", response.get("Error"));
    }

    @Test
    @Order(3)
    public void testLogin_Success() {
        serverFacade.register("testUser", "password123", "test@example.com");
        Map<String, Object> response = serverFacade.login("testUser", "password123");
        assertNotNull(response);
        assertTrue(response.containsKey("success") && (boolean) response.get("success"));
    }

    @Test
    @Order(4)
    public void testLogin_IncorrectPassword() {
        serverFacade.register("testUser2", "password123", "test2@example.com");
        Map<String, Object> response = serverFacade.login("testUser2", "wrongPassword");
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Unauthorized", response.get("Error"));
    }

    @Test
    @Order(5)
    public void testLogout_Success() {
        serverFacade.register("logoutUser", "password123", "logout@example.com");
        serverFacade.login("logoutUser", "password123");
        Map<String, Object> response = serverFacade.logout();
        assertNotNull(response);
        assertTrue(response.containsKey("success") && (boolean) response.get("success"));
    }

    @Test
    @Order(6)
    public void testLogout_NoAuthToken() {
        serverFacade.setAuthToken(null);
        Map<String, Object> response = serverFacade.logout();
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Unauthorized", response.get("Error"));
    }

    @Test
    @Order(7)
    public void testCreateGame_Success() {
        serverFacade.register("gameCreator", "password123", "game@example.com");
        serverFacade.login("gameCreator", "password123");
        Map<String, Object> response = serverFacade.createGame("Chess Game 1");
        assertNotNull(response);
        assertTrue(response.containsKey("success") && (boolean) response.get("success"));
    }

    @Test
    @Order(8)
    public void testCreateGame_NoAuthToken() {
        serverFacade.setAuthToken(null);
        Map<String, Object> response = serverFacade.createGame("Chess Game 2");
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Unauthorized", response.get("Error"));
    }

    @Test
    @Order(9)
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
    public void testListGames_NoAuthToken() {
        serverFacade.setAuthToken(null);
        Map<String, Object> response = serverFacade.listGames();
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Unauthorized", response.get("Error"));
    }

}
