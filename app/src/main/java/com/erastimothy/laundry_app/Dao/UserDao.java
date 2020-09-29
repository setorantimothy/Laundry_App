package com.erastimothy.laundry_app.Dao;

import com.erastimothy.laundry_app.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDao {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public UserDao() {
        this.mAuth = FirebaseAuth.getInstance();
        //get root database
        this.database = FirebaseDatabase.getInstance();
        //set table
        this.reference = database.getReference("users");
    }

}
