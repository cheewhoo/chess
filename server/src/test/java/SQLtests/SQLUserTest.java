package SQLtests;

import dataaccess.*;
import model.DataUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class SQLUserTest {

    private UserDAO userDAO;
    private DataUser validUser;
    private DataUser duplicateUser;

    @BeforeEach
    void initialize() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        userDAO = new SQLUser();
        resetTable();
        validUser = new DataUser("validUser", "validPass", "valid@test.com");
        duplicateUser = new DataUser("validUser", "newPass", "newEmail@test.com");
    }

    @AfterEach
    void cleanup() throws SQLException, DataAccessException {
        resetTable();
    }

    private void resetTable() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement("TRUNCATE user")) {
            stmt.executeUpdate();
        }
    }

    @Test
    void MakeUserSuccessful() throws DataAccessException, UserAlreadyExistsException {
        userDAO.makeUser(validUser);
        DataUser userFromDB = userDAO.getUser(validUser.username());

        assertEquals(validUser.username(), userFromDB.username(),
                "The username should match the inserted user.");
        assertEquals(validUser.password(), userFromDB.password(),
                "The password should match the inserted user.");
        assertEquals(validUser.email(), userFromDB.email(),
                "The email should match the inserted user.");
    }

    @Test
    void FailMakingDuplicateUser() throws DataAccessException, UserAlreadyExistsException {
        userDAO.makeUser(validUser);
        assertThrows(DataAccessException.class, () -> {
            if (userDAO.userExists(validUser.username())) {
                userDAO.makeUser(duplicateUser);
            }
        }, "Adding a duplicate user should throw a DataAccessException.");
    }

    @Test
    void ExistingUserWorks() throws DataAccessException, UserAlreadyExistsException {
        userDAO.makeUser(validUser);  // Insert the user
        DataUser retrieved = userDAO.getUser(validUser.username());
        assertEquals(validUser.username(), retrieved.username(),
                "The username should match the retrieved user.");
        assertEquals(validUser.email(), retrieved.email(),
                "The email should match the retrieved user.");
    }

    @Test
    void NonExistentUser() {
        assertThrows(DataAccessException.class, () -> userDAO.getUser("unknownUser"),
                "Retrieving a non-existent user should throw a DataAccessException.");
    }

    @Test
    void AuthValidCredentials() throws DataAccessException, UserAlreadyExistsException {
        userDAO.makeUser(validUser);
        boolean isAuthenticated = userDAO.authUser(validUser.username(), validUser.password());

        assertTrue(isAuthenticated, "Authentication should succeed with correct credentials.");
    }

    @Test
    void FailAuthInvalidPassword() throws DataAccessException, UserAlreadyExistsException {
        userDAO.makeUser(validUser);
        boolean isAuthenticated = userDAO.authUser(validUser.username(), "wrongPassword");

        assertFalse(isAuthenticated, "Authentication should fail with incorrect password.");
    }


    @Test
    void ConfirmUserExists() throws DataAccessException, UserAlreadyExistsException {
        userDAO.makeUser(validUser);
        assertTrue(userDAO.userExists(validUser.username()),
                "UserExists should return true for an existing user.");
    }

    @Test
    void ReturnFalseForNonExistentUser() throws DataAccessException {
        assertFalse(userDAO.userExists("nonExistentUser"),
                "UserExists should return false for a non-existent user.");
    }

    @Test
    void ClearUserTableSuccessful() throws DataAccessException, SQLException, UserAlreadyExistsException {
        userDAO.makeUser(validUser);
        userDAO.clear();

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement("SELECT * FROM user WHERE username=?")) {
            stmt.setString(1, validUser.username());
            try (var resultSet = stmt.executeQuery()) {
                assertFalse(resultSet.next(),
                        "The table should be empty after calling clear().");
            }
        }
    }

    @Test
    void shouldNotFailClearingEmptyTable() {
        assertDoesNotThrow(() -> userDAO.clear(),
                "Clearing an empty table should not throw any exceptions.");
    }
}
