package test;

import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.ListGameInformation;
import server.Server;
import ui.ResponseException;
import ui.serverfacade;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static serverfacade serverFacade;

    private static final RegisterRequest sergeRequest = new RegisterRequest("Serge", "serge666", "sjh666@byu.edu");
    private static final LoginRequest sergeLoginRequest = new LoginRequest("Serge", "Serge666");


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new serverfacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    public void setUp() throws ResponseException {
        serverFacade.clear();
    }

    @AfterEach
    public void tearDown() throws ResponseException {
        serverFacade.clear();
    }

    @Test
    @Order(1)
    @DisplayName("register (+)")
    public void registerPositive() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        String harperAuth = serverFacade.register(new RegisterRequest("harper", "harper666",
                "xinyu99@gmail.com")).authToken();
        String victorAuth = serverFacade.register(new RegisterRequest("victor", "victor666",
                "gonzo@thegreat.com")).authToken();
        serverFacade.logout(sergeAuth);
        serverFacade.logout(harperAuth);
        serverFacade.logout(victorAuth);
    }

    @Test
    @Order(2)
    @DisplayName("register (-)")
    public void registerNegative() {
        ResponseException exception = assertThrows(ResponseException.class, () ->
                serverFacade.register(new RegisterRequest("", null, "")));
        assertEquals("Server returned: 400 Bad Request", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("login (+)")
    public void loginPositive() throws ResponseException {
        String SergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.logout(SergeAuth);
        assertInstanceOf(String.class, serverFacade.login(sergeLoginRequest).authToken());
    }

    @Test
    @Order(4)
    @DisplayName("login (-)")
    public void loginNegative() {
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.login(sergeLoginRequest));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("logout (+)")
    public void logoutPositive() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.logout(sergeAuth);
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.logout(sergeAuth));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("listGames (+)")
    public void listGamesPositive() throws ResponseException {
        String SergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.createGame(new CreateGameRequest("The Imitation Game"), SergeAuth);
        serverFacade.createGame(new CreateGameRequest("A Hat that says Gamer"), SergeAuth);
        serverFacade.createGame(new CreateGameRequest("Animal Well"), SergeAuth);
        ArrayList<ListGameInformation> games = serverFacade.listGames(SergeAuth).games();
        assertEquals(new ListGameInformation(2, null, null, "A Hat that says Gamer"),
                games.get(1));
    }

    @Test
    @Order(8)
    @DisplayName("(listGames (-)")
    public void listGamesNegative() {
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.listGames("ll"));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("createGame (+)")
    public void createGamePositive() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.createGame(new CreateGameRequest("The Imitation Game"), sergeAuth);
        int gamerGameID = serverFacade.createGame(new CreateGameRequest("A Hat that says Gamer"), sergeAuth).gameID();
        serverFacade.createGame(new CreateGameRequest("Animal Well"), sergeAuth);
        ArrayList<ListGameInformation> games = serverFacade.listGames(sergeAuth).games();
        assertEquals(gamerGameID, games.get(1).gameID());
    }

    @Test
    @Order(10)
    @DisplayName("createGame (-)")
    public void createGameNegative() {
        ResponseException exception = assertThrows(ResponseException.class, () ->
                serverFacade.createGame(new CreateGameRequest("New Game"), "hehe"));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }


    @Test
    @Order(11)
    @DisplayName("joinGame (+)")
    public void joinGamePositive() throws ResponseException {
        String harperAuth = serverFacade.register(new RegisterRequest("harper", "harper666",
                "xinyu99@gmail.com")).authToken();
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        int gameID = serverFacade.createGame(new CreateGameRequest("Muppet Showdown"), sergeAuth).gameID();
        serverFacade.joinGame(new JoinGameRequest("WHITE", gameID), harperAuth);
        serverFacade.joinGame(new JoinGameRequest("BLACK", gameID), sergeAuth);
        ListGameInformation muppetShowdownInfo = serverFacade.listGames(harperAuth).games().getFirst();
        assertEquals(new ListGameInformation(1, "harper", "Serge", "Muppet Showdown"), muppetShowdownInfo);
    }

    @Test
    @Order(12)
    @DisplayName("joinGame (-)")
    public void joinGameNegative() throws ResponseException {
        String fozzieAuth = serverFacade.register(sergeRequest).authToken();
        int gameID = serverFacade.createGame(new CreateGameRequest("Geri's Game"), fozzieAuth).gameID();
        ResponseException exception = assertThrows(ResponseException.class, () ->
                serverFacade.joinGame(new JoinGameRequest("GREEN", gameID), fozzieAuth));
        assertEquals("Server returned: 400 Bad Request", exception.getMessage());
    }

    @Test
    @Order(13)
    @DisplayName("clear (+)")
    public void clearPositive() throws ResponseException {
        String fozzieAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.createGame(new CreateGameRequest("The Imitation Game"), fozzieAuth);
        serverFacade.createGame(new CreateGameRequest("A Hat that says Gamer"), fozzieAuth);
        serverFacade.createGame(new CreateGameRequest("Animal Well"), fozzieAuth);
        serverFacade.clear();
        String kermitAuth = serverFacade.register(new RegisterRequest("kermit", "beinggreenisprettycoolngl",
                "kermit@muppets.com")).authToken();
        assertEquals(0, serverFacade.listGames(kermitAuth).games().size());
    }

    @Test
    @Order(14)
    @DisplayName("clear (-)")
    public void clearNegative() throws ResponseException{
        String fozzieAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.clear();
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.logout(fozzieAuth));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }






}