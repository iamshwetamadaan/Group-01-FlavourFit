package com.flavourfit.Resources;

import com.flavourfit.User.UserDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public final class Helpers {
    public static Properties getAppProperties() throws IOException {
        Properties appProperties = new Properties();
        InputStream stream = new FileInputStream(Paths.get(Constants.PROPERTIES_FILE_URL).toString());
        appProperties.load(stream);
        return appProperties;
    }

    public static boolean isValidUser(UserDto user) {
        if (user == null) {
            return false;
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return false;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return false;
        }

        return true;
    }


}
