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
class AuthTest {
    private final AuthService auth_service = new AuthService();

    private final ClearService do_service = new ClearService();

    private final GameService game_service = new GameService();

    private String third_Auth;

    AuthTest() throws DataAccessException {
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
    @DisplayName("register (+)")
    void register_positive() throws DataAccessException, IllegalAccessException {
        String tylerAuth = this.auth_service.register(new RegisterRequest(
                "Su", "lala", "Sulala@GG.com")).authToken();
        this.auth_service.logout(tylerAuth);
    }

    @Test
    @Order(2)
    @DisplayName("register (-)")
    void register_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.auth_service.register(new RegisterRequest("Su", null, "Sulala@GG.com")));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("login (+)")
    void login_positive() throws DataAccessException, IllegalAccessException, SQLException {
        String newAuth = this.auth_service.login(new LoginRequest("serge999", "111213")).authToken();
        this.auth_service.logout(newAuth);
    }

    @Test
    @Order(4)
    @DisplayName("login (-)")
    void login_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.auth_service.login(new LoginRequest("serge", "111213")));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("logout (+)")
    void logout_positive() throws DataAccessException, IllegalAccessException {
        this.auth_service.logout(this.third_Auth);
    }

    @Test
    @Order(6)
    @DisplayName("logout (-)")
    void logout_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.auth_service.logout(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }
}