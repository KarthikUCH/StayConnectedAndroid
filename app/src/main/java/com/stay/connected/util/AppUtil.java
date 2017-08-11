package com.stay.connected.util;

import android.text.TextUtils;

/**
 * Created by karthikeyan on 8/8/17.
 */

public class AppUtil {

    private static final String PATTERN_EMAIL = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static final String PATTERN_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    private static final String SPECIAL_CHAR_SET = "@%+\\/'!#$^?:.,(){}[]~`-_]";

    public static boolean verifyUserEmail(String userEmail) {
        if (TextUtils.isEmpty(userEmail)) {
            return false;
        }
        return userEmail.matches(PATTERN_EMAIL);
    }


    /**
     * To validate is user password contains at least single special character
     *
     * @param s
     * @return
     */
    private static boolean containsSpecialChar(String s) {
        if (s == null) return false;
        for (int i = 0; i < SPECIAL_CHAR_SET.length(); i++) {
            if (s.contains(SPECIAL_CHAR_SET.subSequence(i, i + 1))) {
                return true;
            }
        }
        return false;
    }


    public static boolean verifyUserPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        if (password.matches(PATTERN_PASSWORD) && containsSpecialChar(password)) {
            return true;
        }
        return false;
    }


}
