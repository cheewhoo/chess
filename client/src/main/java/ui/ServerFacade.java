package ui;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ServerFacade {
    private String baseURL;
    private String authToken;

    public ServerFacade(String baseURL) {
        this.baseURL = baseURL;
    }

    public Map<String, Object> register(String username, String password, String email) {
        Map<String, Object> response = sendRequest("POST", "/user", Map.of("username", username, "password", password, "email", email));
        if (response.containsKey("Error")) {
            return Map.of("error", response.get("Error"));
        }
        return Map.of("success", true);
    }


    public Map<String, Object> login(String username, String password) {
        // Attempt to login with provided credentials
        Map<String, Object> response = sendRequest("POST", "/session", Map.of("username", username, "password", password));

        // Check if the response contains the authToken (successful login)
        if (response.containsKey("authToken")) {
            setAuthToken((String) response.get("authToken"));
            return Map.of("success", true); // Return success message for successful login
        }

        // If login failed, provide an error message with details
        if (response.containsKey("Error")) {
            return Map.of("error", response.get("Error")); // Specific error message from the server
        }

        // Return a generic error message if no specific error info is available
        return Map.of("error", "Login failed: Unexpected response from server");
    }



    public Map<String, Object> logout() {
        return sendRequest("DELETE", "/session", null);
    }

    public Map<String, Object> createGame(String gameName) {
        return sendRequest("POST", "/game", Map.of("gameName", gameName));
    }

    public Map<String, Object> listGames() {
        return sendRequest("GET", "/game", null);
    }

    private Map<String, Object> sendRequest(String method, String endpoint, Map<String, String> body) {
        Map<String, Object> responseMap;
        try {
            URI uri = new URI(baseURL + endpoint);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            if (body != null) {
                http.setDoOutput(true);
                http.addRequestProperty("Content-Type", "application/json");
                try (var outputStream = http.getOutputStream()) {
                    outputStream.write(new Gson().toJson(body).getBytes());
                }
            }

            http.connect();

            if (http.getResponseCode() == 401) {
                return Map.of("Error", "Unauthorized");
            }

            if (http.getResponseCode() == 403) {
                return Map.of("Error", "User already exists");
            }

            if (http.getResponseCode() != 200) {
                return Map.of("Error", "Unexpected response code: " + http.getResponseCode());
            }

            try (InputStream respBody = http.getInputStream()) {
                responseMap = new Gson().fromJson(new InputStreamReader(respBody), Map.class);
            }

        } catch (URISyntaxException e) {
            return Map.of("Error", "Invalid URI: " + e.getMessage());
        } catch (IOException e) {
            return Map.of("Error", "Connection issue: " + e.getMessage());
        }
        return responseMap;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
