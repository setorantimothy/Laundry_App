package com.erastimothy.laundry_app.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.erastimothy.laundry_app.Dao.SharedPreferencesUser;
import com.erastimothy.laundry_app.Model.User;
import com.erastimothy.laundry_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class OrderLaundryActivity extends AppCompatActivity {

    private TextInputLayout dropDownLayout;
    private AutoCompleteTextView dropDownText;
    private SharedPreferencesUser userSP;
    private double harga = 0;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_laundry);
        userSP = new SharedPreferencesUser(OrderLaundryActivity.this);
        user = userSP.getUserLoginFromSharedPrefernces();

        TextInputEditText nama_et = findViewById(R.id.nama_et);
        TextInputEditText kuantitas_et = findViewById(R.id.quantity_et);
        TextInputEditText harga_et = findViewById(R.id.harga_et);
        TextInputEditText ongkir_et = findViewById(R.id.ongkir_et);
        TextInputEditText total_et = findViewById(R.id.total_et);
        MaterialButton order_btn = findViewById(R.id.btnOrder);

        dropDownLayout = findViewById(R.id.jenis_ddl);
        dropDownText = findViewById(R.id.jenis_dd);

        //list items
        String[] items = new String[] {
                "Kiloan",
                "Sprei",
                "Boneka",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            OrderLaundryActivity.this,
                R.layout.jenis_dropdown,
                items
        );

        dropDownText.setAdapter(adapter);
        nama_et.setText(user.getName());

        kuantitas_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(!kuantitas_et.getText().toString().isEmpty())
                    harga_et.setText(String.valueOf(harga * Float.parseFloat(kuantitas_et.getText().toString().trim())));
                else
                    harga_et.setText("0");
            }
        });
        
        dropDownText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapter.getItem(i)){
                    case "Kiloan":
                        harga = 5000;
                        break;
                    case "Sprei":
                        harga = 9000;
                        break;
                    case "Boneka":
                        harga = 15000;
                        break;
                }
                if(!kuantitas_et.getText().toString().isEmpty())
                    harga_et.setText(String.valueOf(harga * Float.parseFloat(kuantitas_et.getText().toString().trim())));
                else{
                    harga_et.setText("0");
                    kuantitas_et.setText("1");
                }
            }
        });

    }

}