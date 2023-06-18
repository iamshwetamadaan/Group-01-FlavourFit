package User;

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
}
