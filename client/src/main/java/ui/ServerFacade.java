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
        return sendRequest("POST", "/user", Map.of("username", username, "password", password, "email", email));
    }

    public Map<String, Object> login(String username, String password) {
        return sendRequest("POST", "/session", Map.of("username", username, "password", password));
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

            try (InputStream respBody = http.getInputStream()) {
                responseMap = new Gson().fromJson(new InputStreamReader(respBody), Map.class);
            }

        } catch (URISyntaxException | IOException e) {
            return Map.of("Error", e.getMessage());
        }
        return responseMap;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
