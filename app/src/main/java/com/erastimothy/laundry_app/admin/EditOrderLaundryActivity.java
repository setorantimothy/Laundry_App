package com.erastimothy.laundry_app.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.dao.LaundryDao;
import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.model.Toko;
import com.erastimothy.laundry_app.model.User;
import com.erastimothy.laundry_app.preferences.TokoPreferences;
import com.erastimothy.laundry_app.preferences.UserPreferences;
import com.erastimothy.laundry_app.user.OrderDetailActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfMeasurement;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class EditOrderLaundryActivity extends AppCompatActivity {

    private TextInputLayout dropDownLayout;
    private AutoCompleteTextView dropDownText;
    private AutoCompleteTextView status_dd;
    private UserPreferences userSP;
    private TokoPreferences tokoSP;
    private double harga = 0;
    private Toko toko;
    private Laundry laundry;
    private LaundryDao laundryDao;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_edit_order_laundry);
        userSP = new UserPreferences(EditOrderLaundryActivity.this);
        laundryDao = new LaundryDao(EditOrderLaundryActivity.this);
        tokoSP = new TokoPreferences(EditOrderLaundryActivity.this);
        toko = tokoSP.getToko();
        user = userSP.getUserLoginFromSharedPrefernces();


        TextInputEditText nama_et = findViewById(R.id.nama_et);
        TextView title_tv = findViewById(R.id.title_tv);
        TextInputEditText kuantitas_et = findViewById(R.id.quantity_et);
        TextInputEditText harga_et = findViewById(R.id.harga_et);
        TextInputEditText ongkir_et = findViewById(R.id.ongkir_et);
        TextInputEditText total_et = findViewById(R.id.total_et);
        TextInputEditText alamat_et = findViewById(R.id.alamat_et);
        MaterialButton order_btn = findViewById(R.id.btnOrder);

        dropDownLayout = findViewById(R.id.jenis_ddl);
        dropDownText = findViewById(R.id.jenis_dd);
        status_dd = findViewById(R.id.status_dd);

        Laundry laundry = (Laundry) getIntent().getSerializableExtra("laundry");
        Bundle bundle = getIntent().getBundleExtra("laundry");
        String orderId = bundle.getString("order_id");
        String tanggal = bundle.getString("tanggal");
        String user_order_uid = bundle.getString("uid");

        title_tv.setText("#"+orderId);
        nama_et.setText(bundle.getString("nama"));
        alamat_et.setText(bundle.getString("alamat"));
        dropDownText.setText(bundle.getString("jenis"));
        status_dd.setText(bundle.getString("status"));
        kuantitas_et.setText(bundle.getString("kuantitas"));
        harga_et.setText(bundle.getString("harga"));
        ongkir_et.setText(bundle.getString("biaya_antar"));
        total_et.setText(bundle.getString("total_pembayaran"));


        //list items
        String[] items = new String[]{
                "Cuci Kiloan",
                "Cuci Sprei Satuan",
                "Cuci Boneka Satuan",
        };

        String[] status_lists = new String[]{
                "Pesanan Batal",
                "Menunggu Penjemputan",
                "Sedang  Diproses",
                "Pesanan Selesai",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                EditOrderLaundryActivity.this,
                R.layout.jenis_dropdown,
                items
        );
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(
                EditOrderLaundryActivity.this,
                R.layout.jenis_dropdown,
                status_lists
        );

        dropDownText.setAdapter(adapter);
        status_dd.setAdapter(adapterStatus);

        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){

                    Laundry laundry = new Laundry(user_order_uid,nama_et.getText().toString().trim(),
                            dropDownText.getText().toString(),alamat_et.getText().toString().trim(),
                            status_dd.getText().toString(), LocalDate.now().toString(),"null",Double.parseDouble(kuantitas_et.getText().toString()),
                            Double.parseDouble(harga_et.getText().toString()),Double.parseDouble(ongkir_et.getText().toString()),
                            Double.parseDouble(total_et.getText().toString()));
                    laundryDao = new LaundryDao(EditOrderLaundryActivity.this);
                    //laundryDao.reset();
                    laundryDao.save(laundry,orderId);
                    laundryDao.setAllDataLaundry();

                    goToOrderDetail(laundry);

                    clearForm();
                }
            }
        });


        kuantitas_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!kuantitas_et.getText().toString().isEmpty()) {
                    hitungHarga();
                } else
                    harga_et.setText("0");
            }
        });

        dropDownText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapter.getItem(i)) {
                    case "Cuci Kiloan":
                        harga = 5000;
                        break;
                    case "Cuci Sprei Satuan":
                        harga = 9000;
                        break;
                    case "Cuci Boneka Satuan":
                        harga = 15000;
                        break;
                }
                if (!kuantitas_et.getText().toString().isEmpty()) {
                    hitungHarga();
                } else {
                    harga_et.setText("0");
                    kuantitas_et.setText("1");
                }
            }
        });

    }

    private void goToOrderDetail(Laundry laundry) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("alamat",laundry.getAlamat());
        bundle.putString("biaya_antar",String.valueOf(laundry.getBiaya_antar()));
        bundle.putString("harga",String.valueOf(laundry.getHarga()));
        bundle.putString("total_pembayaran",String.valueOf(laundry.getTotal_pembayaran()));
        bundle.putString("jenis",laundry.getJenis());
        bundle.putString("kuantitas", String.valueOf(laundry.getKuantitas()));
        bundle.putString("order_id",laundry.getOrder_id());
        bundle.putString("nama",laundry.getNama());
        bundle.putString("tanggal",laundry.getTanggal());
        bundle.putString("uid",laundry.getUid());
        bundle.putString("status",laundry.getStatus());
        intent.putExtra("laundry",bundle);

        startActivity(intent);
        finish();
    }

    private void clearForm(){
        TextInputEditText kuantitas_et = findViewById(R.id.quantity_et);
        TextInputEditText harga_et = findViewById(R.id.harga_et);
        TextInputEditText ongkir_et = findViewById(R.id.ongkir_et);
        TextInputEditText total_et = findViewById(R.id.total_et);
        TextInputEditText alamat_et = findViewById(R.id.alamat_et);

        kuantitas_et.setText("");
        harga_et.setText("");
        ongkir_et.setText("");
        total_et.setText("");
        total_et.setText("");

    }
    private boolean validateForm(){
        TextInputEditText nama_et = findViewById(R.id.nama_et);
        TextInputEditText kuantitas_et = findViewById(R.id.quantity_et);
        TextInputEditText harga_et = findViewById(R.id.harga_et);
        TextInputEditText ongkir_et = findViewById(R.id.ongkir_et);
        TextInputEditText total_et = findViewById(R.id.total_et);
        TextInputEditText alamat_et = findViewById(R.id.alamat_et);

        if(nama_et.getText().length() < 4){
            Toast.makeText(this, "Nama minimal 4 karakter", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(kuantitas_et.getText().length() > 0 && Double.parseDouble(kuantitas_et.getText().toString()) < 1){
            Toast.makeText(this, "Minimal order 1 kg", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(harga_et.getText().length() > 0 && Double.parseDouble(harga_et.getText().toString()) < 0){
            Toast.makeText(this, "Isi form dengan sesuai!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ongkir_et.getText().length() < 0){
            Toast.makeText(this, "Isi form dengan sesuai!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(total_et.getText().length() < 0){
            Toast.makeText(this, "Isi form dengan sesuai!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(alamat_et.getText().length() < 10){
            Toast.makeText(this, "Isi alamat dengan sesuai!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void hitungHarga() {
        TextInputEditText kuantitas_et = findViewById(R.id.quantity_et);
        TextInputEditText harga_et = findViewById(R.id.harga_et);
        TextInputEditText ongkir_et = findViewById(R.id.ongkir_et);
        TextInputEditText total_et = findViewById(R.id.total_et);

        double hrg = Math.round(harga * Float.parseFloat(kuantitas_et.getText().toString().trim()));
        harga_et.setText(String.valueOf(hrg));

        if(validateAddress()){

            double totalHarga = Math.round(hrg+Double.parseDouble(ongkir_et.getText().toString()));
            total_et.setText(String.valueOf(totalHarga));
        }

    }

    private void changeAlamatET(String placeName) {
        TextInputEditText alamat_et = findViewById(R.id.alamat_et);
        alamat_et.setText(placeName);
    }

    private boolean validateAddress(){
        TextInputEditText alamat_et = findViewById(R.id.alamat_et);

        if(alamat_et.getText().toString().trim() != null)
            return true;
        else
            return false;
    }

}