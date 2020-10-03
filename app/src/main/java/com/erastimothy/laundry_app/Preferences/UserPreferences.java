package com.erastimothy.laundry_app.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.erastimothy.laundry_app.Model.User;

public class UserPreferences {
    SharedPreferences userSP;
    SharedPreferences.Editor editor;
    Context context;
    User user;

    private static final String IS_LOGIN = "isLoggedIn";

    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "pasword";
    public static final String KEY_UID = "uid";
    public static final String KEY_PHONENUMBER = "phoneNumber";
    public static final String KEY_OWNER = "_owner";

    public UserPreferences(Context context) {
        this.context = context;
        userSP = context.getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        editor = userSP.edit();
    }

    public void createLoginUser(String uid, String email, String password, String name, String phoneNumber, Boolean owner) {
        //assign user login
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_UID, uid);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PHONENUMBER, phoneNumber);
        editor.putBoolean(KEY_OWNER, owner);

        editor.commit();
    }

    public User getUserLoginFromSharedPrefernces() {
        String uid, email, password, name, phoneNumber;
        Boolean owner;
        uid = userSP.getString(KEY_UID, null);
        name = userSP.getString(KEY_NAME, null);
        email = userSP.getString(KEY_EMAIL, null);
        password = userSP.getString(KEY_PASSWORD, null);
        phoneNumber = userSP.getString(KEY_PHONENUMBER, null);
        owner = userSP.getBoolean(KEY_OWNER, false);

        user = new User(uid, name, email, password, phoneNumber, owner);
        return user;
    }

    public boolean checkLogin() {

        if (userSP.getBoolean(IS_LOGIN, true)) {
            return true;
        } else
            return false;
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }
}
