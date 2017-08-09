package com.stay.connected.application;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by karthikeyan on 8/8/17.
 */

public class AppPreference {

    private static final String TAG = AppPreference.class.getName();

    // USER DETAILS
    private static final String PARAM_USER_NAME = "user_name";
    private static final String PARAM_USER_EMAIL = "user_email";
    private static final String PARAM_IS_USER_VERIFIED = "is_user_verified";


    private final Context mContext;

    public AppPreference(Context context) {
        mContext = context;
    }


    public void setUserName(String userName) {
        set(mContext, PARAM_USER_NAME, userName);
    }

    public String getUserName() {
        return get(mContext, PARAM_USER_NAME, "");
    }

    public void setUserEmail(String userEmail) {
        set(mContext, PARAM_USER_EMAIL, userEmail);
    }

    public String getUserEmail() {
        return get(mContext, PARAM_USER_EMAIL, "");
    }

    public void setUserVerificationState(boolean isVerified) {
        set(mContext, PARAM_IS_USER_VERIFIED, isVerified);
    }

    public boolean getUserVerificationState() {
        return get(mContext, PARAM_IS_USER_VERIFIED, false);
    }

    /**
     * Get string value from default shared preference file
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    private static String get(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, defaultValue);
    }

    /**
     * Set string value to the default shared preference
     *
     * @param context
     * @param key
     * @param value
     */
    private static void set(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply();
    }

    /**
     * Set boolean value to the default shared preference
     *
     * @param context
     * @param key
     * @param value
     */
    private static void set(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    /**
     * Get boolean value from the default shared preference
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    private static boolean get(Context context, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(key, defaultValue);
    }
}
