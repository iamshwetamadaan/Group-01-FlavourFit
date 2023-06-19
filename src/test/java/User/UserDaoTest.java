package User;

import com.flavourfit.User.User;
import com.flavourfit.User.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        userDao = new UserDao();
    }

    @Test
    public void userExistsTest() {
        // Test when userId is null
        assertFalse(userDao.userExists(null));

        // Test when userId is empty
        assertFalse(userDao.userExists(""));

        // Test when user does not exist
        assertFalse(userDao.userExists("User-999"));

        // Test when user exists
        assertTrue(userDao.userExists("User-1"));
    }

    @Test
    public void getUserByIdTest() {
        // Test when userId is null
        assertNull(userDao.getUserById(null));

        // Test when userId is empty
        assertNull(userDao.getUserById(""));

        // Test when user does not exist
        assertNull(userDao.getUserById("User-999"));

        // Test when user exists
        User user = userDao.getUserById("User-1");
        assertNotNull(user);
        assertEquals("User-1", user.getUserId());
    }
}
