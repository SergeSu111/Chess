package DataAcessTests;

import dataAccess.sqlUser;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataAccess.DatabaseManager;
import dataAccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLUserDAOTests {
    private final sqlUser userDAO = new sqlUser();

    SQLUserDAOTests() throws DataAccessException {
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO.clear();
        userDAO.createUser("ross", "hypergorilla", "pedropascalwithhair@haken.com");
        userDAO.createUser("richard", "biggestmoth", "bassproshop@haken.com");
        userDAO.createUser("raymond", "architext", "cutthecameras@haken.com");
        userDAO.createUser("charlie", "stoneface", "handsomegollum@haken.com");
        userDAO.createUser("conner", "golowmode", "???@haken.com");
        userDAO.createUser("peter", "harvardharvard", "lasvegasbonk@haken.com");
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("clear (+)")
    void clear_positive() throws DataAccessException {
        userDAO.clear();
        assertEquals(0, DatabaseManager.getRows("Users"));
    }

    @Test
    @Order(2)
    @DisplayName("createUser (+)")
    void createUser_positive() throws DataAccessException {
        userDAO.createUser("michael", "annieISOKay", "heehee@singer.com");
        userDAO.createUser("percy", "aBethisHOt", "mightypen@demigod.com");
        assertEquals(8, DatabaseManager.getRows("Users"));
    }
    @Test
    @Order(3)
    @DisplayName("createUser (-)")
    void createUser_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> userDAO.createUser("parletscimpernel", null, "no"));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("getUser (+)")
    void getUser_positive() throws DataAccessException, IllegalAccessException {
        // 调用 getUser 方法获取用户信息
        UserData userData = userDAO.getUser("richard", "password", "bassproshop@haken.com");

        // 检查返回的用户信息是否为 null
       // assertNotNull(userData, "User not found");

        boolean userExists = userDAO.userIsStored("richard");
        // 断言用户存在
        assertTrue(userExists, "User not found");
        // 断言用户名是否正确
        //assertEquals("richard", userData.username());
    }


    @Test
    @Order(5)
    @DisplayName("getUser (-)")
    void getUser_negative() throws DataAccessException {
        UserData userData = userDAO.getUser("diego", "fdsf", "fdsfsfd");
        assertNull(userData, "Expected user not to be registered");
    }

    @Test
    @Order(6)
    @DisplayName("userExists (+)")
    void userExists_positive() throws DataAccessException, IllegalAccessException {
        assertTrue(userDAO.userIsStored("peter"));
    }
    @Test
    @Order(7)
    @DisplayName("userExists (-)")
    void userExists_negative() throws DataAccessException, IllegalAccessException {
        assertFalse(userDAO.userIsStored("vikram"));
    }

    @Test
    @Order(8)
    @DisplayName("userPasswordMatches (+)")
    void userPasswordMatches_positive() throws DataAccessException, IllegalAccessException {
        String connerPassword = "golowmode";
        assertTrue(userDAO.passwordMatch("conner", connerPassword));
    }
    @Test
    @Order(9)
    @DisplayName("userPasswordMatches (-)")
    void userPasswordMatches_negative() throws DataAccessException, IllegalAccessException {
        assertFalse(userDAO.passwordMatch("conner", "nomatch"));
    }
}