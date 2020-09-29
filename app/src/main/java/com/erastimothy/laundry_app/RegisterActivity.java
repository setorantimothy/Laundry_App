package com.erastimothy.laundry_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erastimothy.laundry_app.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText name_et,email_et,phoneNumber_et,password_et;
    MaterialButton signUp,signIn;
    FirebaseDatabase database;
    DatabaseReference reference;
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

        mAuth = FirebaseAuth.getInstance();
        //get root database
        database = FirebaseDatabase.getInstance();
        //set table
        reference = database.getReference("users");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    registerAuth(email_et.getText().toString(), password_et.getText().toString());
                }
            }
        });

    }


    private void registerUsers(String uid){
        String email = email_et.getText().toString();
        String name = name_et.getText().toString();
        String password = password_et.getText().toString();
        String phoneNumber = phoneNumber_et.getText().toString();
        boolean is_owner = false;
        User user = new User(uid,name,email,password,phoneNumber,is_owner);
        reference.child(uid).setValue(user);
        clearForm();

    }

    private void registerAuth(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Register Successfull , please login !", Toast.LENGTH_SHORT).show();
                            FirebaseUser fUser = mAuth.getCurrentUser();

                            //register user to database realtime
                            registerUsers(fUser.getUid());

                            //biar ga auto login
                            mAuth.signOut();
                        }else {
                            Toast.makeText(RegisterActivity.this, "Register Failed : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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