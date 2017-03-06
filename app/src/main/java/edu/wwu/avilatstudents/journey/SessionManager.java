package edu.wwu.avilatstudents.journey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by avila_000 on 3/1/2017.
 */

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private final int PRIVATE_MODE = 0;
    private static boolean IS_LOGGED_IN;
    private static final String SP_NAME = "JourneySP";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AUTHENTICATION = "authentication_token";


    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SP_NAME, PRIVATE_MODE);
        IS_LOGGED_IN = false;
        editor = sharedPreferences.edit();
    }

    public void saveUsername(String username){
        editor.putString(KEY_USERNAME, username);
        editor.commit();
        IS_LOGGED_IN = true;
    }

    public void saveEmail(String email){
        editor.putString(KEY_EMAIL, email);
        editor.commit();
        IS_LOGGED_IN = true;
    }

    public void saveAuthentication(String authentication){
        editor.putString(KEY_AUTHENTICATION, authentication);
        editor.commit();
    }

    public String getUsername(){return sharedPreferences.getString(KEY_USERNAME, null);}

    public String getEmail(){return sharedPreferences.getString(KEY_EMAIL, null);}

    public String getAuthentication(){return sharedPreferences.getString(KEY_AUTHENTICATION, null);}

    public boolean isLoggedIn(){return (sharedPreferences.getString(KEY_USERNAME, null) != null);}

    public void login(){
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        if(isLoggedIn()) return;
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
