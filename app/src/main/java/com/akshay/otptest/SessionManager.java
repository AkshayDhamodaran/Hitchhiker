package com.akshay.otptest;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_DOB = "dateOfBirth";
    public static final String KEY_PHONENO = "phoneNumber";
    public static final String KEY_COLLEGE = "college";

    public SessionManager(Context _context) {
        context = _context;
        usersSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String firstname, String lastname, String dateOfBirth, String phoneNumber,String college) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FIRSTNAME, firstname);
        editor.putString(KEY_LASTNAME, lastname);
        editor.putString(KEY_DOB, dateOfBirth);
        editor.putString(KEY_PHONENO, phoneNumber);
        editor.putString(KEY_COLLEGE, college);

        editor.commit();
    }

    public HashMap<String, String> getUserDetailsFromSessions() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_FIRSTNAME, usersSession.getString(KEY_FIRSTNAME, null));
        userData.put(KEY_LASTNAME, usersSession.getString(KEY_LASTNAME, null));
        userData.put(KEY_DOB, usersSession.getString(KEY_DOB, null));
        userData.put(KEY_PHONENO, usersSession.getString(KEY_PHONENO, null));
        userData.put(KEY_COLLEGE, usersSession.getString(KEY_COLLEGE, null));

        return userData;

    }

    public boolean checkLogin() {
        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void logoutUserfromSession(){
        editor.clear();
        editor.commit();
    }
}
