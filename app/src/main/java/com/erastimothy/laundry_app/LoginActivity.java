package com.erastimothy.laundry_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.erastimothy.laundry_app.Admin.AdminMainActivity;
import com.erastimothy.laundry_app.Dao.UserDao;
import com.erastimothy.laundry_app.User.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

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