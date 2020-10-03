package com.erastimothy.laundry_app.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.erastimothy.laundry_app.Model.Toko;
import com.erastimothy.laundry_app.Preferences.TokoPreferences;
import com.erastimothy.laundry_app.R;
import com.google.android.material.textfield.TextInputEditText;

public class PengaturanTokoActivity extends AppCompatActivity {
    private TokoPreferences tokoSP;
    private Toko toko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_toko);
        tokoSP = new TokoPreferences(this);
        toko = tokoSP.getToko();

        TextInputEditText namaToko_et = findViewById(R.id.namaToko_et);
        TextInputEditText alamatToko_et = findViewById(R.id.alamat_et);
        TextInputEditText telp_et = findViewById(R.id.telp_et);

        if(toko.getName().trim() != null){
            namaToko_et.setText(toko.getName());
            alamatToko_et.setText(toko.getAlamat());
            telp_et.setText(toko.getTelp());
        }


    }
}