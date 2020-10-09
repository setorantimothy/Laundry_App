package com.erastimothy.laundry_app.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.dao.LaundryDao;
import com.erastimothy.laundry_app.dao.LayananDao;
import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.model.Layanan;
import com.erastimothy.laundry_app.preferences.LayananPreferences;
import com.erastimothy.laundry_app.preferences.TokoPreferences;
import com.erastimothy.laundry_app.preferences.UserPreferences;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LayananFormActivity extends AppCompatActivity {
    private TextInputEditText nama_et, harga_et;
    private MaterialButton save_btn, delete_btn;
    private LayananPreferences layananSP;
    private LayananDao layananDao;
    private Layanan layanan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan_form);

        layananDao = new LayananDao(this);
        layananSP = new LayananPreferences(this);

        TextView title_tv = findViewById(R.id.title_tv);
        nama_et = findViewById(R.id.nama_et);
        harga_et = findViewById(R.id.harga_et);
        save_btn = findViewById(R.id.btnSave);
        delete_btn = findViewById(R.id.btnDelete);

        layanan = (Layanan) getIntent().getSerializableExtra("layanan");
        Bundle bundle = getIntent().getBundleExtra("layanan");
        if (bundle == null) {
            title_tv.setText("Tambah Layanan");
            delete_btn.setVisibility(View.GONE);
        } else {
            title_tv.setText("Upadate Layanan #" + bundle.getString("id"));
            nama_et.setText(bundle.getString("nama"));
            harga_et.setText(bundle.getString("harga"));
            save_btn.setText("Simpan");
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layanan = new Layanan(nama_et.getText().toString().trim(), 0, Double.parseDouble(harga_et.getText().toString().trim()));

                if(bundle == null){ //save data
                    layananDao.save(layanan, null);
                }else { //update data
                    layananDao.save(layanan,bundle.getString("id"));
                }
                clearForm();
                LayananFormActivity.super.onBackPressed();
                Toast.makeText(LayananFormActivity.this, "Please refresh data to update data", Toast.LENGTH_SHORT).show();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LayananFormActivity.this);
                builder.setMessage("Yakin ingin menghapus layanan ? ");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        layananDao.deleteLayanan(bundle.getString("id"));
                        clearForm();
                        LayananFormActivity.super.onBackPressed();
                        Toast.makeText(LayananFormActivity.this, "Please refresh data to update data", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void clearForm(){
        nama_et.setText("");
        harga_et.setText("");
    }
}