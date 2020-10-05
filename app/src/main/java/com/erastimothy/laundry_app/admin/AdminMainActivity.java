package com.erastimothy.laundry_app.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.erastimothy.laundry_app.dao.LaundryDao;
import com.erastimothy.laundry_app.dao.UserDao;
import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.preferences.LaundryPreferences;
import com.erastimothy.laundry_app.preferences.UserPreferences;
import com.erastimothy.laundry_app.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {
    List<Laundry> laundryList;
    int totalPesanan=0;
    double totalPendapatan=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDao userDao = new UserDao(this);
        LaundryDao laundryDao = new LaundryDao(this);
        laundryDao.setAllDataLaundry();

        laundryList = new ArrayList<>();

        UserPreferences sessionUser = new UserPreferences(AdminMainActivity.this);
        LaundryPreferences laundryPreferences = new LaundryPreferences(AdminMainActivity.this);
        laundryList = laundryPreferences.getListLaundryFromSharedPreferences();

        if(laundryList!=null){
            for(int i=0; i< laundryList.size(); i++){
                if(laundryList.get(i).getStatus().equalsIgnoreCase("Pesanan Selesai")){
                    totalPesanan++;
                    totalPendapatan += laundryList.get(i).getTotal_pembayaran();
                }
            }
        }


        setContentView(R.layout.activity_admin_main);
        MaterialCardView menu_orderan,menu_keluar,menu_riwayat,menu_PengaturanToko;
        TextView pendapatan_tv = findViewById(R.id.pendapatan_tv);
        TextView pesanan_tv = findViewById(R.id.pesanan_tv);
        TextView nama_tv = findViewById(R.id.nama_txt);
        menu_keluar = findViewById(R.id.menu_keluar);
        menu_orderan = findViewById(R.id.menu_orderan);
        menu_PengaturanToko = findViewById(R.id.menu_pengaturanToko);

        nama_tv.setText(sessionUser.getUserLoginFromSharedPrefernces().getName());
        pesanan_tv.setText(String.valueOf(totalPesanan)+" Pesanan");
        pendapatan_tv.setText("Rp. "+String.valueOf(totalPendapatan));


        menu_PengaturanToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, PengaturanTokoActivity.class);
                startActivity(intent);
            }
        });

        menu_orderan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, PengaturanTokoActivity.class);
                startActivity(intent);
            }
        });

        menu_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMainActivity.this, "Bye, " + sessionUser.getUserLoginFromSharedPrefernces().getName(), Toast.LENGTH_SHORT).show();
                sessionUser.logout();
                userDao.signOut();
            }
        });

    }
}