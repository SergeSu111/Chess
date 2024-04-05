package clientTests;

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


public class ServerFacadeTests {

    private static Server server;
    private static serverfacade serverFacade;

    private static final RegisterRequest sergeRequest = new RegisterRequest("Serge", "serge666", "sjh666@byu.edu");
    private static final LoginRequest sergeLoginRequest = new LoginRequest("Serge", "serge666");


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new serverfacade(port);
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
        serverFacade.logout(sergeAuth);
        serverFacade.logout(harperAuth);
        serverFacade.logout(victorAuth);
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
        serverFacade.logout(SergeAuth);
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
        serverFacade.logout(sergeAuth);
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout(sergeAuth));
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("logout (-)")
    public void logoutNegative() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout("hehe"));
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("listGames (+)")
    public void listGamesPositive() throws ResponseException {
        String SergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.createGame(new CreateGameRequest("Game1"), SergeAuth);
        serverFacade.createGame(new CreateGameRequest("Game2"), SergeAuth);
        serverFacade.createGame(new CreateGameRequest("Game3"), SergeAuth);
        ArrayList<ListGameInformation> games = serverFacade.listGames(SergeAuth).games();
        Assertions.assertEquals(new ListGameInformation(games.get(1).gameID(), null, null, games.get(1).gameName()),
                games.get(1));
    }

    @Test
    @Order(8)
    @DisplayName("(listGames (-)")
    public void listGamesNegative() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames("ll"));
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("createGame (+)")
    public void createGamePositive() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.createGame(new CreateGameRequest("Game1"), sergeAuth);
        int gamerGameID = serverFacade.createGame(new CreateGameRequest("game2"), sergeAuth).gameID();
        serverFacade.createGame(new CreateGameRequest("Game3"), sergeAuth);
        ArrayList<ListGameInformation> games = serverFacade.listGames(sergeAuth).games();
        Assertions.assertEquals(games.get(1).gameID(

        ), games.get(1).gameID());
    }

    @Test
    @Order(10)
    @DisplayName("createGame (-)")
    public void createGameNegative() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.createGame(new CreateGameRequest("New Game"), "hehe"));
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }


    @Test
    @Order(11)
    @DisplayName("joinGame (+)")
    public void joinGamePositive() throws ResponseException {
        String harperAuth = serverFacade.register(new RegisterRequest("harper", "harper666",
                "xinyu99@gmail.com")).authToken();
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        int gameID = serverFacade.createGame(new CreateGameRequest("Game2"), sergeAuth).gameID();
        serverFacade.joinGame(new JoinGameRequest("WHITE", gameID), harperAuth);
        serverFacade.joinGame(new JoinGameRequest("BLACK", gameID), sergeAuth);
        ListGameInformation muppetShowdownInfo = serverFacade.listGames(harperAuth).games().getFirst();
        Assertions.assertEquals(new ListGameInformation(gameID, "harper", "Serge", "Game2"), muppetShowdownInfo);
    }

    @Test
    @Order(12)
    @DisplayName("joinGame (-)")
    public void joinGameNegative() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        int gameID = serverFacade.createGame(new CreateGameRequest("Game4"), sergeAuth).gameID();
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.joinGame(new JoinGameRequest("YELLOW", gameID), sergeAuth));
        Assertions.assertEquals("Server returned: 400 Bad Request", exception.getMessage());
    }

    @Test
    @Order(13)
    @DisplayName("clear (+)")
    public void clearPositive() throws ResponseException {
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.createGame(new CreateGameRequest("The Imitation Game"), sergeAuth);
        serverFacade.createGame(new CreateGameRequest("A Hat that says Gamer"), sergeAuth);
        serverFacade.createGame(new CreateGameRequest("Animal Well"), sergeAuth);
        serverFacade.clear();
        String kermitAuth = serverFacade.register(new RegisterRequest("harper", "harper666",
                "xinyu99@gmail.com")).authToken();
        Assertions.assertEquals(0, serverFacade.listGames(kermitAuth).games().size());
    }

    @Test
    @Order(14)
    @DisplayName("clear (-)")
    public void clearNegative() throws ResponseException{
        String sergeAuth = serverFacade.register(sergeRequest).authToken();
        serverFacade.clear();
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout(sergeAuth));
        Assertions.assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }






}
