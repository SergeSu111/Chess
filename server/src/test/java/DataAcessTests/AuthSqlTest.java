package DataAcessTests;

import com.google.gson.Gson;
import dataAccess.SQLAuth;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import dataAccess.DatabaseManager;
import dataAccess.DataAccessException;
import model.AuthData;
import chess.ChessGame;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLAuthDAOTests {
    private final SQLAuth authDAO = new SQLAuth();

    private String rossAuthToken;

    SQLAuthDAOTests() throws DataAccessException {
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        authDAO.clear();
        rossAuthToken = authDAO.createAuth("Bei");
        authDAO.createAuth("Alice");
        authDAO.createAuth("Bob");
        authDAO.createAuth("Cas");
        authDAO.createAuth("Annie");
        authDAO.createAuth("Issac");
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("clear (+)")
    void clear_positive() throws DataAccessException {
        authDAO.clear();
        assertEquals(0, DatabaseManager.getRows("Auths"));
    }

    @Test
    @Order(2)
    @DisplayName("createAuth (+)")
    void createAuth_positive() throws DataAccessException {
        authDAO.createAuth("echo8358");
        authDAO.createAuth("davidisamanwithalargesandwich");
        authDAO.createAuth("lorinwithano");
        assertEquals(9, DatabaseManager.getRows("Auths"));
    }

    @Test
    @Order(3)
    @DisplayName("createAuth (-)")
    void createAuth_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDAO.createAuth(new Gson().toJson(new ChessGame())));
        assertEquals("Data truncation: Data too long for column 'username' at row 1", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("getAuth (+)")
    void getAuth_positive() throws DataAccessException, IllegalAccessException {
        AuthData testData = authDAO.getAuth(rossAuthToken);
        AuthData actualData = new AuthData(rossAuthToken, "Bei");
        assertEquals(actualData, testData);
    }

    @Test
    @Order(5)
    @DisplayName("getAuth (-)")
    void getAuth_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDAO.getAuth(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("getUsername (+)")
    void getUsername_positive() throws DataAccessException, IllegalAccessException {
        assertEquals("Bei", authDAO.getUserName(rossAuthToken));
    }

    @Test
    @Order(7)
    @DisplayName("getUsername (-)")
    void getUsername_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDAO.getUserName(UUID.randomUUID().toString()));
        assertEquals("Error: Username not exist", exception.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("authExists (+)")
    void authExists_positive() throws DataAccessException, IllegalAccessException {
        assertTrue(authDAO.authIsStored(rossAuthToken));
    }

    @Test
    @Order(9)
    @DisplayName("authExists (-)")
    void authExists_negative() {
//        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDAO));
    }

    @Test
    @Order(10)
    @DisplayName("deleteAuth (+)")
    void deleteAuth_positive() throws DataAccessException, IllegalAccessException {
        authDAO.deleteAuth(rossAuthToken);
        assertFalse(authDAO.authIsStored(rossAuthToken));
    }

//    @Test
//    @Order(11)
//    @DisplayName("deleteAuth (-)")
//    void deleteAuth_negative() throws DataAccessException {
//       String validAuthToken = "valid_auth_token";
//
//    }
}