package com.example.android1.todo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView signuptextview;
    String email, password;
    DatabaseHelper databaseHelper;
    ArrayList<TypeUser> allUserList;
    String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        allUserList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(LoginActivity.this);


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

        editTextEmail = (EditText) findViewById(R.id.edittextemail);
        editTextPassword = (EditText) findViewById(R.id.edittextpassword);
        signuptextview = (TextView) findViewById(R.id.signuptextview);
        buttonLogin = (Button) findViewById(R.id.buttonlogin);
        buttonLogin.setOnClickListener(this);
        signuptextview.setOnClickListener(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonlogin:
                loginMethod();
                break;
            case R.id.signuptextview:
                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(in);
                finish();

        }
    }

    private void loginMethod() {
        hideKeyboard();


        ScrollView linearLayout = (ScrollView) findViewById(R.id.loginlayout);
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        int flag = 1;
        if (email.isEmpty()) {
            flag = 0;
            editTextEmail.setError("email can not blank");
        }

        if (password.isEmpty()) {
            flag = 0;
            editTextPassword.setError("email can not blank");
        }

        if (flag == 1) {
            int login_status = 1;
            for (int i = 0; i < allUserList.size(); i++) {
                String dummy_password = allUserList.get(i).getPassword();
                String dummy_email = allUserList.get(i).getEmail();

                if (password.equals(dummy_password))
                    if (email.equals(dummy_email)) {
                        login_status = 0;
                        Snackbar.make(linearLayout, "Successfully Login", Snackbar.LENGTH_LONG).show();
                        Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                        in.putExtra("id", allUserList.get(i).getId());
                        startActivity(in);
                        finish();
                    }
            }

            if (login_status == 1)
                Snackbar.make(linearLayout, "User doesn't Exists", Snackbar.LENGTH_LONG).show();

        }
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

