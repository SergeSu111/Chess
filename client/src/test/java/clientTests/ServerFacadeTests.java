package clientTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import result.CreateGameResult;
import server.Server;
import ui.ResponseException;
import ui.ServerFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @BeforeAll
    public static void clearAll() throws ResponseException, DataAccessException {
        serverFacade.clear();
    }

    @Test
    @Order(1)
    public void registerPos() throws ResponseException, DataAccessException {
        serverFacade.clear();
        String username = "user";
        String password = "password";
        String email = "email";
        assertDoesNotThrow(() -> serverFacade.register(username, password, email));
    }

    @Test
    @Order(2)
    public void registerNeg() throws ResponseException{
        String username = "user";
        String password = "password";
        String email = null;
        assertThrows(ResponseException.class, ()-> serverFacade.register(username,password,email));
    }

    @Test
    @Order(3)
    public void loginPos() throws ResponseException{
        String username = "user";
        String password = "password";
        assertDoesNotThrow(() -> serverFacade.login(username,password));
    }

    @Test
    @Order(4)
    public void loginNeg() throws ResponseException{
        String username = "user";
        String password = "Wrongpassword";
        assertThrows(ResponseException.class, () -> serverFacade.login(username,password));
    }

    @Test
    @Order(5)
    public void createGamePos() throws ResponseException{
        String gameName = "NewGame";
        assertDoesNotThrow(()-> serverFacade.createGame(gameName));
    }

    @Test
    @Order(6)
    public void createGameNeg() throws ResponseException{
        String gameName = null;
        assertThrows(ResponseException.class,()-> serverFacade.createGame(gameName));
    }
    @Test
    @Order(7)
    public void listGamePos() throws ResponseException, DataAccessException {
        String gameName = "game2";
        serverFacade.createGame(gameName);
        assertDoesNotThrow(()-> serverFacade.listGames());
    }

    @Test
    @Order(8)
    public void listGameNeg() throws ResponseException{
        ServerFacade nullUrl = new ServerFacade(null);
        assertThrows(ResponseException.class, ()-> nullUrl.listGames());
    }

    @Test
    @Order(9)
    public void joinGamePos() throws ResponseException, DataAccessException {
        String username = "user";
        String password = "password";
        serverFacade.login(username, password);
        String gameName = "NewGame";
        CreateGameResult newgame = serverFacade.createGame(gameName);
        assertDoesNotThrow(()-> serverFacade.joinGame(newgame.gameID(), "white"));
    }

    @Test
    @Order(10)
    public void joinGameNeg() throws ResponseException{
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(- 1, "white"));
    }

    @Test
    @Order(11)
    public void logoutPos() throws ResponseException, DataAccessException {
        String username = "user2";
        String password = "password2";
        String email = "email2";
        serverFacade.register(username,password,email);
        serverFacade.login(username,password);
        assertDoesNotThrow(() -> serverFacade.logout());
    }

    @Test
    @Order(12)
    public void logoutNeg() throws ResponseException{
        ServerFacade emptyUrl = new ServerFacade(null);
        assertThrows(ResponseException.class, emptyUrl::logout);
    }
}