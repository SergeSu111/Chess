package serviceTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.UUID;

import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import service.AuthService;

import service.ClearService;

import service.GameService;

import dataAccess.DataAccessException;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DBoperationTest {
    private final AuthService auth_service = new AuthService();

    private final ClearService do_service = new ClearService();

    private final GameService game_service = new GameService();

    private String third_Auth;

    DBoperationTest() throws DataAccessException {
    }

    @BeforeEach
    void setUp() throws DataAccessException, IllegalAccessException {
        String first_Auth = this.auth_service.register(new RegisterRequest(
                "serge666", "Sjh200105010501", "sjh@byu.edu")).authToken();
        String second_Auth = this.auth_service.register(new RegisterRequest(
                "serge777", "12345", "982536473@qq.com")).authToken();
        this.third_Auth = this.auth_service.register(new RegisterRequest(
                "serge888", "678910", "third@fsd.com")).authToken();
        String forth_Auth = this.auth_service.register(new RegisterRequest(
                "serge999", "111213", "forth@fds.com")).authToken();
        this.auth_service.logout(forth_Auth);
        this.game_service.createGame(new CreateGameRequest("game1"), this.third_Auth);
        this.game_service.createGame(new CreateGameRequest("game2"), second_Auth);
        this.game_service.createGame(new CreateGameRequest("game3"), first_Auth);
    }

    @AfterEach
    void tearDown() throws DataAccessException, SQLException {
        this.do_service.clear();
    }

    @Test
    @Order(1)
    @DisplayName("clear (+)")
    void clear_positive() throws DataAccessException, SQLException {
        this.do_service.clear();
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.auth_service.logout(this.third_Auth));
        assertEquals("Error: unauthorized", exception.getMessage());
    }
}