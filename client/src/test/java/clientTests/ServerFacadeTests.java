package clientTests;

import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.ListGameInformation;
import server.Server;
import ui.ResponseException;
import ui.ServerFacade;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    private static final RegisterRequest sergeRequest = new RegisterRequest("Serge", "serge666", "sjh666@byu.edu");
    private static final LoginRequest sergeLoginRequest = new LoginRequest("Serge", "serge666");


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer()
    {
        server.stop();
    }


    @BeforeEach
    public void clear() throws ResponseException {
        serverFacade.clear();
    }

    @AfterEach
    public void shotDown() throws ResponseException {
        serverFacade.clear();
    }

    @Test
    @Order(1)
    @DisplayName("register (+)")
    public void registerNice() throws ResponseException
    {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        String harperAuth = serverFacade.register(new RegisterRequest("harper", "harper666",
                "xinyu99@gmail.com")).authToken();
        String victorAuth = serverFacade.register(new RegisterRequest("victor", "victor666",
                "gonzo@thegreat.com")).authToken();
        serverFacade.logout();
        serverFacade.logout();
        serverFacade.logout();
    }

    @Test
    @Order(2)
    @DisplayName("register (-)")
    public void registerNegative() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.register(new RegisterRequest("", null, "")));
        Assertions.assertEquals("Server returned: 400 Bad Request", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("login (+)")
    public void loginPositive() throws ResponseException {
        String SergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.logout();
        Assertions.assertInstanceOf(String.class, serverFacade.login(sergeLoginRequest).authToken());
    }

    @Test
    @Order(4)
    @DisplayName("login (-)")
    public void loginNegative() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> serverFacade.login(sergeLoginRequest));
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("logout (+)")
    public void logoutPositive() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.logout();
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout());
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("logout (-)")
    public void logoutNegative() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout());
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("listGames (+)")
    public void listGamesPositive() throws ResponseException {
        String SergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.createGame(new CreateGameRequest("Game1"));
        serverFacade.createGame(new CreateGameRequest("Game2"));
        serverFacade.createGame(new CreateGameRequest("Game3"));
        ArrayList<ListGameInformation> games = serverFacade.listGames().games();
        Assertions.assertEquals(new ListGameInformation(games.get(1).gameID(), null, null, games.get(1).gameName()),
                games.get(1));
    }

    @Test
    @Order(8)
    @DisplayName("(listGames (-)")
    public void listGamesNegative() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames());
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("createGame (+)")
    public void createGamePositive() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.createGame(new CreateGameRequest("Game1"));
        int gamerGameID = serverFacade.createGame(new CreateGameRequest("game2")).gameID();
        serverFacade.createGame(new CreateGameRequest("Game3"));
        ArrayList<ListGameInformation> games = serverFacade.listGames().games();
        Assertions.assertEquals(games.get(1).gameID(

        ), games.get(1).gameID());
    }

    @Test
    @Order(10)
    @DisplayName("createGame (-)")
    public void createGameNegative() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.createGame(new CreateGameRequest("New Game")));
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }


    @Test
    @Order(11)
    @DisplayName("joinGame (+)")
    public void joinGamePositive() throws ResponseException {
        String harperAuth = serverFacade.register(new RegisterRequest("harper", "harper666",
                "xinyu99@gmail.com")).authToken();
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        int gameID = serverFacade.createGame(new CreateGameRequest("Game2")).gameID();
        serverFacade.joinGame(new JoinGameRequest("WHITE", gameID));
        serverFacade.joinGame(new JoinGameRequest("BLACK", gameID));
        ListGameInformation muppetShowdownInfo = serverFacade.listGames().games().getFirst();
        Assertions.assertEquals(new ListGameInformation(gameID, "harper", "Serge", "Game2"), muppetShowdownInfo);
    }

    @Test
    @Order(12)
    @DisplayName("joinGame (-)")
    public void joinGameNegative() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        int gameID = serverFacade.createGame(new CreateGameRequest("Game4")).gameID();
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.joinGame(new JoinGameRequest("YELLOW", gameID)));
        Assertions.assertEquals("Server returned: 400 Bad Request", exception.getMessage());
    }

    @Test
    @Order(13)
    @DisplayName("clear (+)")
    public void clearPositive() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.createGame(new CreateGameRequest("The Imitation Game"));
        serverFacade.createGame(new CreateGameRequest("A Hat that says Gamer"));
        serverFacade.createGame(new CreateGameRequest("Animal Well"));
        serverFacade.clear();
        String kermitAuth = serverFacade.register(new RegisterRequest("harper", "harper666",
                "xinyu99@gmail.com")).authToken();
        Assertions.assertEquals(0, serverFacade.listGames().games().size());
    }

    @Test
    @Order(14)
    @DisplayName("clear (-)")
    public void clearNegative() throws ResponseException{
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.clear();
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout());
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }






}
