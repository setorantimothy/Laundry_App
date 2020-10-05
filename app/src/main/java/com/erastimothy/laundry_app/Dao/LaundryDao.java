package com.erastimothy.laundry_app.Dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.erastimothy.laundry_app.Model.Laundry;
import com.erastimothy.laundry_app.Model.Toko;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public LaundryDao(Activity myActivity){
        activity = myActivity;
        mAuth = FirebaseAuth.getInstance();
        //get root database
        database = FirebaseDatabase.getInstance();
        //set table
        reference = database.getReference("laundry");
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");

        laundrySP = myActivity.getSharedPreferences("laundryPreferences",Context.MODE_PRIVATE);
        editor = laundrySP.edit();
    }
    public void reset(){
        reference.setValue("init data");
    }
    public void save(Laundry laundry){
        Random random = new Random();
        String rand = String.valueOf(random.nextInt(1000-1)+100);
        String order_id = String.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"))+rand);
        laundry.setOrder_id(order_id);
        reference.child(order_id).setValue(laundry);
        Toast.makeText(activity, "Orderan berhasil diterima !", Toast.LENGTH_SHORT).show();

    }
}
