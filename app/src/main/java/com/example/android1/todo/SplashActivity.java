package com.example.android1.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

public class SplashActivity extends Activity {
    ///////for shared Preferences
    public static final String LoginPREFERENCES = "LoginPrefs";
    public static final String idKey = "id";
    public static final String emailKey = "emailKey";
    public static final String passwordKey = "urlKey";
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sharedpreferences;


    ArrayList<TypeUser> allUserList;
    DatabaseHelper databaseHelper;
    String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        allUserList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(SplashActivity.this);


        Cursor cursor = databaseHelper.getAllUser();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d(TAG, cursor.getString(1));
                Log.d(TAG, cursor.getString(2));
                Log.d(TAG, cursor.getString(3));
                Log.d(TAG, cursor.getString(4));
                allUserList.add(new TypeUser(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                sharedpreferences = getSharedPreferences(LoginPREFERENCES, Context.MODE_PRIVATE);

                String id = sharedpreferences.getString(idKey, "0");
                String email = sharedpreferences.getString(emailKey, "0");
                String password = sharedpreferences.getString(passwordKey, "0");
                int login_status = 1;
                for (int i = 0; i < allUserList.size(); i++) {
                    String dummy_password = allUserList.get(i).getPassword();
                    String dummy_email = allUserList.get(i).getEmail();

                    if (password.equals(dummy_password))
                        if (email.equals(dummy_email)) {
                            login_status = 0;
                            Intent in = new Intent(SplashActivity.this, HomeActivity.class);
                            in.putExtra("id", id);
                            startActivity(in);
                            finish();
                        }
                }

                if (login_status == 1) {
                    Intent in = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }

}
