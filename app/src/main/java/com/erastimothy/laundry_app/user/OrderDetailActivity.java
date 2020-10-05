package com.erastimothy.laundry_app.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.erastimothy.laundry_app.MainActivity;
import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.model.Toko;
import com.erastimothy.laundry_app.preferences.TokoPreferences;
import com.google.android.material.button.MaterialButton;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView orderid_tv,namaTv,alamatTv,tanggalTv,statusTv,jenisTv,kuantitasTv,hargaTv,totalTv,biayaAntarTv,namaTokoTv,alamatTokoTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TokoPreferences tokoPreferences = new TokoPreferences(this);
        Toko toko = tokoPreferences.getToko();
        super.onCreate(savedInstanceState);


        //set content view
        setContentView(R.layout.activity_order_detail);
        Laundry laundry = (Laundry) getIntent().getSerializableExtra("laundry");
        Bundle bundle = getIntent().getBundleExtra("laundry");
        orderid_tv = findViewById(R.id.orderid_tv);
        namaTv = findViewById(R.id.nama_tv);
        namaTokoTv = findViewById(R.id.namaToko_tv);
        alamatTokoTv = findViewById(R.id.alamatToko_tv);
        alamatTv = findViewById(R.id.alamat_tv);
        tanggalTv = findViewById(R.id.tanggal_tv);
        statusTv = findViewById(R.id.status_tv);
        jenisTv = findViewById(R.id.jenis_tv);
        kuantitasTv = findViewById(R.id.kuantitas_tv);
        hargaTv = findViewById(R.id.harga_tv);
        totalTv = findViewById(R.id.total_tv);
        biayaAntarTv = findViewById(R.id.biaya_antar_tv);
        MaterialButton btnBack = findViewById(R.id.btnBack);

        //set data
        orderid_tv.setText(bundle.getString("order_id"));
        namaTv.setText(bundle.getString("nama"));
        alamatTv.setText(bundle.getString("alamat"));
        tanggalTv.setText(bundle.getString("tanggal"));
        statusTv.setText(bundle.getString("status"));
        jenisTv.setText(bundle.getString("jenis"));
        kuantitasTv.setText(bundle.getString("kuantitas"));
        hargaTv.setText(bundle.getString("harga"));
        biayaAntarTv.setText(bundle.getString("biaya_antar"));
        totalTv.setText(bundle.getString("total_pembayaran"));
        namaTokoTv.setText(toko.getName());
        alamatTokoTv.setText(toko.getAlamat()+"\n"+toko.getTelp());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().hasExtra("from")){
                    startActivity(new Intent(OrderDetailActivity.this, MainActivity.class));
                }
                else
                    OrderDetailActivity.super.onBackPressed();
            }
        });

    }
}