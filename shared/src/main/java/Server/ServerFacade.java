package Server;
import com.google.gson.Gson;
import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.*;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult registerUser(RegisterRequest request) throws DataAccessException{
        var httpRequest = buildRequest("POST","/user",request,null);
        var response = sendRequest(httpRequest);
        return handleResponse(response,RegisterResult.class);
    }

    public LoginResult loginUser(LoginRequest request) throws DataAccessException{
        var httpRequest = buildRequest("POST","/session",request,null);
        var response = sendRequest(httpRequest);
        return handleResponse(response,LoginResult.class);
    }

    public void logout(LogoutRequest request) throws DataAccessException{
        var httpRequest = buildRequest("DELETE","/session",null,request.authToken());
        var response = sendRequest(httpRequest);
        handleResponse(response,null);
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException{
        var httpRequest = buildRequest("POST","/game",request,request.authToken());
        var response = sendRequest(httpRequest);
        return handleResponse(response,CreateGameResult.class);
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException{
        var httpRequest = buildRequest("PUT","/game",request,request.authToken());
        var response = sendRequest(httpRequest);
        handleResponse(response,null);
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException{
        var httpRequest = buildRequest("GET","/game",request,request.authToken());
        var response = sendRequest(httpRequest);
        return handleResponse(response,ListGamesResult.class);
    }


    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if(authToken != null){
            request.header("authorization",authToken);
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws DataAccessException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws DataAccessException {
        var status = response.statusCode();
        if ((status / 100) != 2) {
            var body = response.body();
            if (body != null) {
                throw new DataAccessException(body);
            }
            throw new DataAccessException("other failure: " + status);
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }
}
