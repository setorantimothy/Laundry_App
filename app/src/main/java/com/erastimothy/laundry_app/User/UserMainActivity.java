package com.erastimothy.laundry_app.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.erastimothy.laundry_app.Dao.UserDao;
import com.erastimothy.laundry_app.LoginActivity;
import com.erastimothy.laundry_app.Model.User;
import com.erastimothy.laundry_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserMainActivity extends AppCompatActivity {
    private UserDao userDao;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        userDao = new UserDao(this);

        //get root database
        database = FirebaseDatabase.getInstance();
        //set table
        reference = database.getReference("users");

        RelativeLayout signOutMenu = (RelativeLayout) findViewById(R.id.signOutMenu);
        RelativeLayout myAccountMenu = (RelativeLayout) findViewById(R.id.myAccountMenu);

        signOutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserMainActivity.this, "Bye, "+userDao.getCurrentUid().toString(), Toast.LENGTH_SHORT).show();
                userDao.signOut();
            }
        });

        myAccountMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = reference.child(userDao.getCurrentUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String uid = snapshot.child("uid").getValue(String.class);
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String password = snapshot.child("password").getValue(String.class);
                        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                        Boolean _owner = snapshot.child("_owner").getValue(Boolean.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("uid",uid);
                        bundle.putString("name",name);
                        bundle.putString("email",email);
                        bundle.putString("password",password);
                        bundle.putString("phoneNumber",phoneNumber);
                        bundle.putBoolean("_owner",_owner);
                        Intent intent = new Intent(UserMainActivity.this, MyAccountActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
}