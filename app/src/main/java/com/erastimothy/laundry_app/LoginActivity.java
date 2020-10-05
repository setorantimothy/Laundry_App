package com.erastimothy.laundry_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.erastimothy.laundry_app.dao.UserDao;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText email_et,password_et;
    MaterialButton signUp,signIn;
    UserDao userDao;
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Hook layout
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        signUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.btnSignIn);
        userDao = new UserDao(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    userDao.login(email_et.getText().toString().trim(),password_et.getText().toString().trim());
                }
            }
        });

    }


    private boolean validateForm(){
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);

        if(!email_et.getText().toString().trim().matches(emailPattern)){
            Toast.makeText(this, "Please fill email correctly", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password_et.getText().length() < 6){
            Toast.makeText(this, "Please fill password correctly", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}