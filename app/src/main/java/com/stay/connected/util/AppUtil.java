package com.stay.connected.util;

import android.text.TextUtils;

/**
 * Created by karthikeyan on 8/8/17.
 */

public class AppUtil {

    private static final String PATTERN_EMAIL = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static final String PATTERN_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public static boolean verifyUserEmail(String userEmail) {
        if (TextUtils.isEmpty(userEmail)) {
            return false;
        }
        return userEmail.matches(PATTERN_EMAIL);
    }

    public static boolean verifyUserPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return password.matches(PATTERN_PASSWORD);
    }

}
