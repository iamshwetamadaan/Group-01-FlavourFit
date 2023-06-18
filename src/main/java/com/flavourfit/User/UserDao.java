package com.flavourfit.User;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    Map<String, User> usersList;

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

    public boolean userExists(String userId) {
        if (userId == null || userId.isEmpty()) {
            return false;
        }

        if (usersList.get(userId) == null) {
            return false;
        }

        return true;
    }
}
