package com.erastimothy.laundry_app.Dao;

import android.app.Activity;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;

import com.erastimothy.laundry_app.Admin.PengaturanTokoActivity;
import com.erastimothy.laundry_app.Model.Toko;
import com.erastimothy.laundry_app.Model.User;
import com.erastimothy.laundry_app.Preferences.TokoPreferences;
import com.erastimothy.laundry_app.Preferences.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TokoDao {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Activity activity;
    private ProgressDialog progressDialog;
    private Toko toko;

    public TokoDao(Activity myActivity) {
        activity = myActivity;
        mAuth = FirebaseAuth.getInstance();
        //get root database
        database = FirebaseDatabase.getInstance();
        //set table
        reference = database.getReference("toko");
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
    }

    public void saveToko(Toko toko){
        reference.setValue(toko);
        TokoPreferences tokoPreferences = new TokoPreferences(activity);
        tokoPreferences.createToko(toko.getName(),toko.getAlamat(),toko.getLongitude(),toko.getLatitude(),toko.getTelp());
    }

    public void setTokoFromDatabase(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TokoPreferences tokoPreferences = new TokoPreferences(activity);
                String nama,alamat,telp;
                Double lng,lat;
                nama = snapshot.child("name").getValue(String.class);
                alamat = snapshot.child("alamat").getValue(String.class);
                telp = snapshot.child("telp").getValue(String.class);
                lng = snapshot.child("longitude").getValue(Double.class);
                lat = snapshot.child("latitude").getValue(Double.class);
                tokoPreferences.createToko(nama,alamat,lng,lat,telp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
