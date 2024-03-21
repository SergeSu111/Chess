package test;

import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import server.Server;
import ui.ResponseException;
import ui.serverfacade;

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
    public void setUp() throws ResponseException { serverFacade.clear(); }

    @AfterEach
    public void tearDown() throws ResponseException { serverFacade.clear(); }

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
        ResponseException exception = assertThrows(ResponseException.class, () ->  serverFacade.login(sergeLoginRequest));
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
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
