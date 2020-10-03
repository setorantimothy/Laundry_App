package com.erastimothy.laundry_app.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.erastimothy.laundry_app.Dao.UserDao;
import com.erastimothy.laundry_app.MainActivity;
import com.erastimothy.laundry_app.Preferences.UserPreferences;
import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.User.UserMainActivity;
import com.google.android.material.card.MaterialCardView;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDao userDao = new UserDao(this);
        UserPreferences sessionUser = new UserPreferences(AdminMainActivity.this);

        setContentView(R.layout.activity_admin_main);
        MaterialCardView menu_orderan,menu_keluar,menu_riwayat,menu_PengaturanToko;
        TextView pendapatan_tv = findViewById(R.id.pendapatan_tv);
        TextView pesanan_tv = findViewById(R.id.pesanan_tv);
        TextView nama_tv = findViewById(R.id.nama_txt);
        menu_keluar = findViewById(R.id.menu_keluar);
        menu_PengaturanToko = findViewById(R.id.menu_pengaturanToko);

        nama_tv.setText(sessionUser.getUserLoginFromSharedPrefernces().getName());

        menu_PengaturanToko.setOnClickListener(new View.OnClickListener() {
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