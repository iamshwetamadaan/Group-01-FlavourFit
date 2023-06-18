package com.flavourfit.User;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    Map<String, User> usersList;

    /**
     * This constructor mocks the users in the usersList map
     */
    public UserDao() {
        usersList = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUserId("User-" + (i + 1));
            user.setFirstName("John" + (i + 1));
            user.setLastName("Does" + (i + 1));
            user.setEmail("john" + (i + 1) + "@mail.com");
            user.setContact("123456789");
            user.setCurrentWeight(80d);
            user.setHeight(170d);

            usersList.put(user.getUserId(), user);
        }
    }

    /**
     * Method to check if a user exists in the list
     * @param userId -- String id of the user to be found
     * @return -- true if the user exists
     */
    public boolean userExists(String userId) {
        if (userId == null || userId.isEmpty()) {
            return false;
        }

        if (usersList.get(userId) == null) {
            return false;
        }

        return true;
    }

    /**
     * Method to get a user from list by userId
     * @param userId -- String id of the user to be found
     * @return -- User Object containing the user details if the user exists
     */
    public User getUserById(String userId) {
        if (userId == null || userId.isEmpty()) {
            return null;
        }

        User user = usersList.get(userId);

        return user;
    }
}
