package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.*;

import java.util.List;
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

    @AfterEach
    void cleanup() {
        server.clearDatabase();
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
    public void testRegisterSuccess() {
        Map<String, Object> response = serverFacade.register("newUser", "validPassword", "email@example.com");
        System.out.println("Response: " + response);
        assertNotNull(response, "Should not be null");
        assertTrue(response.containsKey("success"), "Expected 'success', but got: " + response);
        assertEquals(true, response.get("success"), "Expected true, but got: " + response.get("success"));
    }



    @Test
    @Order(2)
    public void testRegisterUserAlreadyExists() {
        serverFacade.register("existingUser", "validPassword", "email@example.com");
        Map<String, Object> response = serverFacade.register("existingUser", "anotherPassword", "email@example.com");
        assertNotNull(response, "Response should not be null");
        assertTrue(response.containsKey("error"), "Expected 'error', but got: " + response);
        assertEquals("already in use", response.get("error"));
    }


    @Test
    @Order(3)
    @DisplayName("Login: Successful Login")
    public void testLoginSuccess() {
        serverFacade.register("testUser", "password123", "test@example.com");
        Map<String, Object> response = serverFacade.login("testUser", "password123");
        assertNotNull(response, "Response should not be null");
        assertTrue(response.containsKey("success"),
                "Expected 'success' key in the response, but got: " + response);
        assertEquals(true, response.get("success"), "Login should be successful");
        String authToken = serverFacade.getAuthToken();
        assertNotNull(authToken, "Auth token should not be null");
        assertFalse(authToken.isEmpty(), "Auth token should not be empty");
    }



    @Test
    @Order(4)
    public void testLoginIncorrectPassword() {
        serverFacade.register("testUser2", "password123", "test2@example.com");
        Map<String, Object> response = serverFacade.login("testUser2", "wrongPassword");
        assertNotNull(response);
        assertTrue(response.containsKey("error"));
        assertEquals("Unauthorized", response.get("error"));
    }

    @Test
    @Order(5)
    public void testLogoutSuccess() {
        serverFacade.register("logoutUser", "password123", "logout@example.com");
        serverFacade.login("logoutUser", "password123");
        Map<String, Object> response = serverFacade.logout();
        System.out.println("Logout response: " + response);
        assertNotNull(response, "Response should not be null");
        assertTrue(response.isEmpty() || (response.containsKey("success") && (boolean) response.get("success")),
                "Expected logout response to indicate success, but got: " + response);
    }


    @Test
    @Order(6)
    public void testLogoutNoAuthToken() {
        serverFacade.setAuthToken(null);
        Map<String, Object> response = serverFacade.logout();
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Unauthorized", response.get("Error"));
    }

    @Test
    @Order(7)
    @DisplayName("Create Game: Successful Game Creation")
    public void testCreateGameSuccess() {
        // Register and login to get an auth token
        serverFacade.register("gameCreator", "password123", "game@example.com");
        serverFacade.login("gameCreator", "password123");
        Map<String, Object> response = serverFacade.createGame("Chess Game 1");
        assertNotNull(response, "Response should not be null");
        assertTrue(response.containsKey("success"), "Expected 'success', but got: " + response);
        assertEquals(true, response.get("success"), "Expected true, but got: " + response.get("success"));
    }


    @Test
    @Order(8)
    public void testCreateGameNoAuthToken() {
        serverFacade.setAuthToken(null);
        Map<String, Object> response = serverFacade.createGame("Chess Game 2");
        assertNotNull(response);
        assertTrue(response.containsKey("error"));
        assertEquals("Unauthorized", response.get("error"));
    }

    @Test
    @Order(9)
    public void testListGamesSuccess() {
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
    public void testListGamesNoAuthToken() {
        serverFacade.setAuthToken(null);
        Map<String, Object> response = serverFacade.listGames();
        assertNotNull(response);
        assertTrue(response.containsKey("Error"));
        assertEquals("Unauthorized", response.get("Error"));
    }



    @Test
    public void testJoinGameSuccess() {
        serverFacade.register("testUser", "password", "email@example.com");
        serverFacade.login("testUser", "password");
        Map<String, Object> createGameResponse = serverFacade.createGame("Test Game");
        assertTrue(createGameResponse.containsKey("success"), "Game creation failed");
        Map<String, Object> listGamesResponse = serverFacade.listGames();
        assertTrue(listGamesResponse.containsKey("games"), "Games list is empty");
        int gameID = ((Double) ((Map<String, Object>) ((List<?>) listGamesResponse.get("games")).get(0)).get("gameID")).intValue();
        Map<String, Object> joinGameResponse = serverFacade.joinGame(String.valueOf(gameID), "white");
        assertTrue(joinGameResponse.containsKey("success") && (boolean) joinGameResponse.get("success"), "Failed to join the game");
    }

    @Test
    public void testObserveGameSuccess() {
        // Mock the setup by registering, logging in, and creating a game
        serverFacade.register("observerUser", "password", "observer@example.com");
        serverFacade.login("observerUser", "password");
        Map<String, Object> createGameResponse = serverFacade.createGame("Observable Game");
        assertTrue(createGameResponse.containsKey("success"), "Game creation failed");
        Map<String, Object> listGamesResponse = serverFacade.listGames();
        assertTrue(listGamesResponse.containsKey("games"), "Games list is empty");
        int gameID = ((Double) ((Map<String, Object>) ((List<?>) listGamesResponse.get("games")).get(0)).get("gameID")).intValue();
        Map<String, Object> observeGameResponse = serverFacade.observeGame(String.valueOf(gameID));
        assertFalse(observeGameResponse.containsKey("error"), "Error while observing game");
    }


    @Test
    public void testJoinGameFailureInvalidColor() {
        serverFacade.register("testUser", "password", "email@example.com");
        serverFacade.login("testUser", "password");
        Map<String, Object> createGameResponse = serverFacade.createGame("Test Game");
        assertTrue(createGameResponse.containsKey("success"), "Game creation failed");
        Map<String, Object> listGamesResponse = serverFacade.listGames();
        assertTrue(listGamesResponse.containsKey("games"), "Games list is empty");
        int gameID = ((Double) ((Map<String, Object>) ((List<?>) listGamesResponse.get("games")).get(0)).get("gameID")).intValue();
        Map<String, Object> joinGameResponse = serverFacade.joinGame(String.valueOf(gameID), "blue");
        assertTrue(joinGameResponse.containsKey("error"), "Expected error for invalid color was not received");
        assertEquals("Invalid color specified. Choose 'white' or 'black'.", joinGameResponse.get("error"), "Error message did not match expected output");
    }

    @Test
    public void testObserveGameFailureNonExistentGame() {
        serverFacade.register("observerUser", "password", "observer@example.com");
        serverFacade.login("observerUser", "password");
        Map<String, Object> observeGameResponse = serverFacade.observeGame("99999");
        assertTrue(observeGameResponse.containsKey("Error"), "Expected error for non-existent game was not received");
        assertEquals(null, observeGameResponse.get("error"), "Error message did not match expected output");
    }

}
