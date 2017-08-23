package com.example.android1.todo;

import android.content.Context;
import android.content.Intent;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextname, editTextemail, editTextpassword, editTextphone, editTextconfirmpassword;
    Button buttonregistration;
    String name, email, password, phone, confirmpassword;
    ScrollView signuplayout;
    DatabaseHelper databaseHelper;
    TextView buttonLogin;

    String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initUI();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    void initUI() {
        editTextname = (EditText) findViewById(R.id.edittextname);
        editTextemail = (EditText) findViewById(R.id.edittextemail);
        editTextpassword = (EditText) findViewById(R.id.edittextpassword);
        editTextphone = (EditText) findViewById(R.id.edittextphone);
        editTextconfirmpassword = (EditText) findViewById(R.id.edittextconfirmpassword);


        buttonregistration = (Button) findViewById(R.id.buttonregistration);
        buttonregistration.setOnClickListener(this);

        buttonLogin = (TextView) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        signuplayout = (ScrollView) findViewById(R.id.signuplayout);

        databaseHelper = new DatabaseHelper(SignupActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonregistration:
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                MethodRegistration();
                break;

            case R.id.buttonLogin:
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
        }
    }

    private void MethodRegistration() {
        name = editTextname.getText().toString();
        email = editTextemail.getText().toString();
        password = editTextpassword.getText().toString();
        phone = editTextphone.getText().toString();
        confirmpassword = editTextconfirmpassword.getText().toString();

        int flag = 1;
        if (name.isEmpty()) {
            flag = 0;
            editTextname.setError("name can not blank");
        }
        if (email.isEmpty()) {
            flag = 0;
            editTextemail.setError("email can not blank");
        }
        if (phone.isEmpty()) {
            flag = 0;
            editTextphone.setError("phone can not blank");
        }
        if (password.isEmpty()) {
            flag = 0;
            editTextpassword.setError("password can not blank");
        }
        if (confirmpassword.isEmpty()) {
            flag = 0;
            editTextconfirmpassword.setError("confirmpassword can not blank");
        }

        if (!password.equals(confirmpassword)) {
            flag = 0;
            Snackbar.make(signuplayout, "Password did't match", Snackbar.LENGTH_LONG).show();

        }

        if (flag == 1) {
            if (databaseHelper.insertUser(name, phone, email, password)) {
                Log.d(TAG, name);
                Log.d(TAG, phone);
                Log.d(TAG, email);
                Log.d(TAG, password);

                Snackbar.make(signuplayout, "Registration Successfuly Completed", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Snackbar.make(signuplayout, "Registration Failed", Snackbar.LENGTH_LONG).show();
            }
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
