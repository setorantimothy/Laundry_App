package com.erastimothy.laundry_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.erastimothy.laundry_app.dao.UserDao;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText name_et,email_et,phoneNumber_et,password_et;
    MaterialButton signUp,signIn;
    UserDao userDao;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hook layout
        name_et = findViewById(R.id.nama_et);
        email_et = findViewById(R.id.email_et);
        phoneNumber_et = findViewById(R.id.phoneNumber_et);
        password_et = findViewById(R.id.password_et);
        signUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.btnSignIn);

        userDao = new UserDao(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    userDao.registerAuth(email_et.getText().toString(), password_et.getText().toString(),name_et.getText().toString(),phoneNumber_et.getText().toString());
                    clearForm();
                }
            }
        });

    }

    private void clearForm(){
        name_et.setText("");
        email_et.setText("");
        phoneNumber_et.setText("");
        password_et.setText("");
    }

    private boolean validateForm(){
        name_et = findViewById(R.id.nama_et);
        email_et = findViewById(R.id.email_et);
        phoneNumber_et = findViewById(R.id.phoneNumber_et);
        password_et = findViewById(R.id.password_et);

        if(name_et.getText().toString().isEmpty() || name_et.getText().length() < 2){
            Toast.makeText(this, "Please fill name correctly", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email_et.getText().toString().trim().matches(emailPattern)){
            Toast.makeText(this, "Please fill email correctly", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(phoneNumber_et.getText().length() < 9){
            Toast.makeText(this, "Phone number min 9 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password_et.getText().length() < 6){
            Toast.makeText(this, "Password minimum 6 character", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}