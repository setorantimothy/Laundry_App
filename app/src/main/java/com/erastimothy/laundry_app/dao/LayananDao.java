package com.erastimothy.laundry_app.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.model.Layanan;
import com.erastimothy.laundry_app.preferences.LaundryPreferences;
import com.erastimothy.laundry_app.preferences.LayananPreferences;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class LayananDao {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Activity activity;
    private Layanan layanan;
    private SharedPreferences layananSP;
    private SharedPreferences.Editor editor;
    private List<Layanan> layananList;

    public LayananDao(Activity myActivity) {
        activity = myActivity;
        mAuth = FirebaseAuth.getInstance();
        //get root database
        database = FirebaseDatabase.getInstance();
        //set table
        reference = database.getReference("layanan");

        layananList = new ArrayList<>();

    }

    public void save(Layanan layanan, String id) {
        LayananPreferences layananPreferences = new LayananPreferences(activity);

        if (id == null) {
            id = String.valueOf(layananPreferences.getLayananMaxId() + 1);
        }

        layanan.setId(Integer.parseInt(id));
        reference.child(id).setValue(layanan);
        Toast.makeText(activity, "Layanan berhasil disimpan !", Toast.LENGTH_SHORT).show();
        layananPreferences.createLayanan(layanan);
        setAllDataLayanan();
    }

    public void setAllDataLayanan() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layananList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Log.i("Layanan name : ", childSnapshot.child("name").getValue(String.class));
                    layananList.add(childSnapshot.getValue(Layanan.class));

                }

                LayananPreferences layananPreferences = new LayananPreferences(activity);
                layananPreferences.setLayananMaxId((int) snapshot.getChildrenCount());
                layananPreferences.setAllLayanan(layananList);
                //Toast.makeText(activity, "MAX ID "+snapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteLayanan(String id){
        reference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "Berhasil menghapus layanan", Toast.LENGTH_SHORT).show();
            }
        });

        setAllDataLayanan();
    }

}
