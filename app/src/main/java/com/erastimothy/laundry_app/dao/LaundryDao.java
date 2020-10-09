package com.erastimothy.laundry_app.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.preferences.LaundryPreferences;
import com.erastimothy.laundry_app.preferences.TokoPreferences;
import com.erastimothy.laundry_app.preferences.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LaundryDao {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Activity activity;
    private ProgressDialog progressDialog;
    private Laundry laundry;
    private SharedPreferences laundrySP;
    private SharedPreferences.Editor editor;
    private List<Laundry> laundryList;

    public LaundryDao(Activity myActivity){
        activity = myActivity;
        mAuth = FirebaseAuth.getInstance();
        //get root database
        database = FirebaseDatabase.getInstance();
        //set table
        reference = database.getReference("laundry");

        laundryList = new ArrayList<>();

    }
    public void reset(){
        reference.setValue("init data");
    }
    public void save(Laundry laundry,String order_id){
        if(order_id.trim().isEmpty()){
            Random random = new Random();
            String rand = String.valueOf(random.nextInt(1000-1)+100);
            order_id = String.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"))+rand);
        }

        laundry.setOrder_id(order_id);
        reference.child(order_id).setValue(laundry);
        Toast.makeText(activity, "Orderan berhasil diterima !", Toast.LENGTH_SHORT).show();
        LaundryPreferences laundryPreferences = new LaundryPreferences(activity);
        laundryPreferences.createLaundry(laundry);
    }

    public void setAllDataLaundry(){
        reference.orderByChild("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    laundryList.add(childSnapshot.getValue(Laundry.class));
                }

                LaundryPreferences laundryPreferences = new LaundryPreferences(activity);
                laundryPreferences.setAllLaundry(laundryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
