package dataAccessTests;

import com.google.gson.Gson;
import dataAccess.sqlGame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataAccess.DatabaseManager;
import dataAccess.DataAccessException;
import model.GameData;
import chess.ChessGame;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLGameDAOTests {
    private final sqlGame gameDAO = new sqlGame();

    private final String defaultGame = new Gson().toJson(new ChessGame());

    private int puzzleChessGameID;

    SQLGameDAOTests() throws DataAccessException {
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame("Ender's Game");
        puzzleChessGameID = gameDAO.createGame("Puzzle Chess");
        gameDAO.createGame("FPS Chess Test");
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        gameDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("clear (+)")
    void clear_positive() throws DataAccessException {
        gameDAO.clear();
        assertEquals(0, DatabaseManager.getRows("Games"));
    }

    @Test
    @Order(2)
    @DisplayName("createGame (+)")
    void createGame_positive() throws DataAccessException {
        gameDAO.createGame("The Game of Love");
        gameDAO.createGame("GameGrumps");
        assertEquals(5, DatabaseManager.getRows("Games"));
    }

    @Test
    @Order(3)
    @DisplayName("createGame (-)")
    void createGame_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.createGame(new Gson().toJson(new ChessGame())));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("addGame (+)")
    void addGame_positive() throws DataAccessException {
        gameDAO.addGame(76, "Conner", "Jack", "The Big Game", "{\"field\":\"hehe\"}");
        assertEquals(4, DatabaseManager.getRows("Games"));
    }

    @Test
    @Order(6)
    @DisplayName("addGame (-)")
    void addGame_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.addGame(-1, null, null, null, null));
        assertEquals("Column 'gameName' cannot be null", exception.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("delGame (+)")
    void delGame_positive() throws DataAccessException, IllegalAccessException {
        gameDAO.delGame(puzzleChessGameID);
        assertEquals(2, DatabaseManager.getRows("Games"));
    }

    @Test
    @Order(8)
    @DisplayName("delGame (-)")
    void delGame_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.delGame(90));
        assertEquals("Error: your game is not existed", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("getGame (+)")
    void getGame_positive() throws DataAccessException {
        String newGameJson = new Gson().toJson(new ChessGame());
        GameData expectedGame = new GameData(puzzleChessGameID, null, null, "Puzzle Chess", newGameJson);
        assertEquals(expectedGame, gameDAO.getGame(puzzleChessGameID));
    }

    @Test
    @Order(5)
    @DisplayName("getGame (-)")
    void getGame_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.getGame(89));
        assertEquals("Error: There is no game in DB", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("gameExists (+)")
    void gameExists_positive() throws DataAccessException, IllegalAccessException {
        assertTrue(gameDAO.gameExists(puzzleChessGameID));
    }

    @Test
    @Order(7)
    @DisplayName("gameExists (-)")
    void gameExists_negative() throws DataAccessException, IllegalAccessException {
        assertFalse(gameDAO.gameExists(745));
    }

    @Test
    @Order(8)
    @DisplayName("listGames (+)")
    void listGames_positive() throws DataAccessException {
        assertEquals(3, gameDAO.listGames().size());
    }

    @Test
    @Order(9)
    @DisplayName("listGames (-)")
    void listGames_negative() throws DataAccessException {
        assertNotEquals(7, gameDAO.listGames().size());
    }

    @Test
    @Order(10)
    @DisplayName("updatePlayerInGame (+)")
    void updatePlayerInGame_positive() throws DataAccessException, IllegalAccessException {
        GameData expectedGame = new GameData(puzzleChessGameID, "baloony", null, "Puzzle Chess", defaultGame);
        gameDAO.updatePlayers(puzzleChessGameID, "baloony", "WHITE");
        assertEquals(expectedGame, gameDAO.getGame(puzzleChessGameID));
    }

    @Test
    @Order(11)
    @DisplayName("updatePlayerInGame (-)")
    void updatePlayerInGame_negative() {
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> gameDAO.updatePlayers(puzzleChessGameID, "dally", "BLUE"));
        assertEquals("Error: invalid color.", exception.getMessage());
    }

    @Test
    @Order(12)
    @DisplayName("colorFreeInGame (+)")
    void colorFreeInGame_positive() throws DataAccessException, IllegalAccessException {
        assertTrue(gameDAO.colorFree("WHITE", puzzleChessGameID));
    }

    @Test
    @Order(13)
    @DisplayName("colorFreeInGame (-)")
    void colorFreeInGame_negative() throws DataAccessException, IllegalAccessException {
        gameDAO.updatePlayers(puzzleChessGameID, "bobby", "WHITE");
        assertFalse(gameDAO.colorFree("WHITE", puzzleChessGameID));
    }

    @Test
    @Order(14)
    @DisplayName("joinGameAsPlayer (+)")
    void joinGameAsPlayer_positive() throws DataAccessException, IllegalAccessException {
        gameDAO.updatePlayers(puzzleChessGameID, "bobby", "WHITE");
        gameDAO.updatePlayers(puzzleChessGameID, "hobby", "BLACK");
        GameData expectedGame = new GameData(puzzleChessGameID, "bobby", "hobby", "Puzzle Chess", defaultGame);
        assertEquals(expectedGame, gameDAO.getGame(puzzleChessGameID));
    }

    @Test
    @Order(15)
    @DisplayName("joinGameAsPlayer (-)")
    void joinGameAsPlayer_negative() throws DataAccessException, IllegalAccessException {
        gameDAO.updatePlayers(puzzleChessGameID, "bobby", "WHITE");
        gameDAO.updatePlayers(puzzleChessGameID, "hobby", "BLACK");
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.joinGame(puzzleChessGameID, "gobby", "WHITE"));
        assertEquals("Error: already taken", exception.getMessage());
    }
}