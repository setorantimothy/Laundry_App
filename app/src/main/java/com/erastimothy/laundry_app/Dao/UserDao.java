package com.erastimothy.laundry_app.Dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.erastimothy.laundry_app.Admin.AdminMainActivity;
import com.erastimothy.laundry_app.LoginActivity;
import com.erastimothy.laundry_app.Model.User;
import com.erastimothy.laundry_app.RegisterActivity;
import com.erastimothy.laundry_app.User.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference,userReff;
    private Activity activity;
    private String name,phoneNumber,email,password;
    private ProgressDialog progressDialog;
    private User user;
    List<User> listUser;

    public UserDao(Activity myActivity) {
        activity = myActivity;
        mAuth = FirebaseAuth.getInstance();
        //get root database
        database = FirebaseDatabase.getInstance();
        //set table
        reference = database.getReference("users");
        listUser = new ArrayList<>();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");

    }

    public void login(String email, String password){
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(activity, "Successful login !", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(activity, "Welcome back "+name, Toast.LENGTH_SHORT).show();

                                        //redirect to owner package
                                        if(_owner == true) {
                                            Intent intent = new Intent(activity, AdminMainActivity.class);
                                            activity.startActivity(intent);

                                        }else {  //redirect to user package
                                            Intent intent = new Intent(activity, UserMainActivity.class);
                                            activity.startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(activity, "Canceled", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                });
    }

    public String getCurrentUid(){
        return mAuth.getUid();
    }

    public void signOut(){
        mAuth.signOut();
        activity.startActivity(new Intent(activity,LoginActivity.class));
        activity.finish();
    }

    private void registerUsers(String uid){
        User user = new User(uid,name,email,password,phoneNumber, false);
        reference.child(uid).setValue(user);
        //biar ga auto login
        mAuth.signOut();

    }

    public void registerAuth(String email, String password,String name,String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(activity, "Register Successfull , please login !", Toast.LENGTH_SHORT).show();
                            FirebaseUser fUser = mAuth.getCurrentUser();

                            //register user to database realtime
                            registerUsers(fUser.getUid());
                        }else {
                            Toast.makeText(activity, "Register Failed : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        progressDialog.dismiss();
    }


}
