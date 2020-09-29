package com.erastimothy.laundry_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText email_et,password_et;
    MaterialButton signUp,signIn;
    FirebaseDatabase database;
    DatabaseReference reference;
    UserDao userDao;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Hook layout
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        signUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.btnSignIn);

        mAuth = FirebaseAuth.getInstance();
        //get root database
        database = FirebaseDatabase.getInstance();
        //set table
        reference = database.getReference("users");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    login(email_et.getText().toString().trim(),password_et.getText().toString().trim());
                }
            }
        });
    }

    private void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Successful login !", Toast.LENGTH_SHORT).show();
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            final String uid = fUser.getUid();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                            Query checkUser = reference.orderByChild("uid").equalTo(uid);

                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String name = snapshot.child(uid).child("name").getValue(String.class);
                                        Boolean _owner = snapshot.child(uid).child("_owner").getValue(Boolean.class);
                                        Toast.makeText(LoginActivity.this, "Welcome back "+name, Toast.LENGTH_SHORT).show();

                                        //redirect to owner package
                                        if(_owner == true) {
                                            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                            startActivity(intent);
                                        }else {
                                            Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                            startActivity(intent);
                                        }
                                        //redirect to user package



                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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