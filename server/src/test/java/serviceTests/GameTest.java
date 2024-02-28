package serviceTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

import request.CreateGameRequest;
import request.JoinGameRequest;
import request.RegisterRequest;
import service.AuthService;

import service.ClearService;
import service.GameService;
import dataAccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameTest {
    private final AuthService auth_service = new AuthService();

    private final ClearService do_service = new ClearService();

    private final GameService game_service = new GameService();

    private String third_Auth;

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
    void tearDown() throws DataAccessException {
        this.do_service.clear();
    }

    @Test
    @Order(1)
    @DisplayName("listGames (+)")
    void listGames_positive() throws DataAccessException, IllegalAccessException {
        int length = this.game_service.listGame(this.third_Auth).games().size();
        assertEquals(3, length);
    }

    @Test
    @Order(2)
    @DisplayName("listGames (-)")
    void listGames_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.game_service.listGame(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("createGame (+)")
    void createGame_positive() throws DataAccessException, IllegalAccessException {
        this.game_service.createGame(new CreateGameRequest("lalala.org"), third_Auth);
        int length = this.game_service.listGame(this.third_Auth).games().size();
        assertEquals(4, length);
    }

    @Test
    @Order(4)
    @DisplayName("createGame (-)")
    void createGame_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                this.game_service.createGame(new CreateGameRequest("lalala.org"), UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("joinGame (+)")
    void joinGame_positive() throws DataAccessException, IllegalAccessException {
        int newGameID = this.game_service.createGame(new CreateGameRequest("lalala.org"), third_Auth).gameID();
        this.game_service.join_game(new JoinGameRequest("BLACK", newGameID), third_Auth);
    }

    @Test
    @Order(6)
    @DisplayName("joinGame (-)")
    void joinGame_negative() throws DataAccessException, IllegalAccessException {
        int newGameID = this.game_service.createGame(new CreateGameRequest("lalala.org"), third_Auth).gameID();
        this.game_service.join_game(new JoinGameRequest("BLACK", newGameID), third_Auth);
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                this.game_service.join_game(new JoinGameRequest("BLACK", newGameID), third_Auth));
        assertEquals("Error: already taken", exception.getMessage());
    }
}