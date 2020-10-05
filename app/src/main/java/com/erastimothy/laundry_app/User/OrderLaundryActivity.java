package com.erastimothy.laundry_app.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.erastimothy.laundry_app.Admin.PengaturanTokoActivity;
import com.erastimothy.laundry_app.Dao.LaundryDao;
import com.erastimothy.laundry_app.Dao.TokoDao;
import com.erastimothy.laundry_app.Model.Laundry;
import com.erastimothy.laundry_app.Model.Toko;
import com.erastimothy.laundry_app.Preferences.TokoPreferences;
import com.erastimothy.laundry_app.Preferences.UserPreferences;
import com.erastimothy.laundry_app.Model.User;
import com.erastimothy.laundry_app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
import static java.lang.Math.round;

public class OrderLaundryActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private TextInputLayout dropDownLayout;
    private AutoCompleteTextView dropDownText;
    private UserPreferences userSP;
    private TokoPreferences tokoSP;
    private double harga = 0;
    private Toko toko;
    private Laundry laundry;
    private LaundryDao laundryDao;
    User user;

    private static final String DESTINATION_SYMBOL_LAYER_ID = "destination-symbol-layer-id";
    private static final String DESTINATION_ICON_ID = "destination-icon-id";
    private static final String DESTINATION_SOURCE_ID = "destination-source-id";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private Point origin,tokoPoint;
    private Marker originMarker,tokoMarker;

    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_order_laundry);
        userSP = new UserPreferences(OrderLaundryActivity.this);
        laundryDao = new LaundryDao(OrderLaundryActivity.this);
        tokoSP = new TokoPreferences(OrderLaundryActivity.this);
        toko = tokoSP.getToko();
        user = userSP.getUserLoginFromSharedPrefernces();

        //laundryDao = new LaundryDao(OrderLaundryActivity.this);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        TextInputEditText nama_et = findViewById(R.id.nama_et);
        TextInputEditText kuantitas_et = findViewById(R.id.quantity_et);
        TextInputEditText harga_et = findViewById(R.id.harga_et);
        TextInputEditText ongkir_et = findViewById(R.id.ongkir_et);
        TextInputEditText total_et = findViewById(R.id.total_et);
        TextInputEditText alamat_et = findViewById(R.id.alamat_et);
        MaterialButton order_btn = findViewById(R.id.btnOrder);

        dropDownLayout = findViewById(R.id.jenis_ddl);
        dropDownText = findViewById(R.id.jenis_dd);

        tokoPoint = Point.fromLngLat(this.toko.getLongitude(), this.toko.getLatitude());


        //list items
        String[] items = new String[]{
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

        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    laundry = new Laundry(user.getUid(),nama_et.getText().toString().trim(),
                            dropDownText.getText().toString(),alamat_et.getText().toString().trim(),
                            "order", LocalDate.now().toString(),"null",Double.parseDouble(kuantitas_et.getText().toString()),
                            Double.parseDouble(harga_et.getText().toString()),Double.parseDouble(ongkir_et.getText().toString()),
                            Double.parseDouble(total_et.getText().toString()));
                    laundryDao = new LaundryDao(OrderLaundryActivity.this);
                    //laundryDao.reset();
                    laundryDao.save(laundry);
                    clearForm();
                }
            }
        });

        alamat_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new PlaceAutocomplete.IntentBuilder()
                            .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                            .placeOptions(PlaceOptions.builder()
                                    .backgroundColor(Color.parseColor("#EEEEEE"))
                                    .limit(10)
                                    .build(PlaceOptions.MODE_CARDS))
                            .build(OrderLaundryActivity.this);
                    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
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
                if (!kuantitas_et.getText().toString().isEmpty()) {
                    hitungHarga();
                } else {
                    harga_et.setText("0");
                    kuantitas_et.setText("1");
                }
            }
        });

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

    private double getJarak() {
        Point pointToko = Point.fromLngLat(toko.getLongitude(), toko.getLatitude());
        return TurfMeasurement.distance(origin, pointToko);
    }

    private void hitungHarga() {
        TextInputEditText kuantitas_et = findViewById(R.id.quantity_et);
        TextInputEditText harga_et = findViewById(R.id.harga_et);
        TextInputEditText ongkir_et = findViewById(R.id.ongkir_et);
        TextInputEditText total_et = findViewById(R.id.total_et);

        double hrg = Math.round(harga * Float.parseFloat(kuantitas_et.getText().toString().trim()));
        harga_et.setText(String.valueOf(hrg));

        if(validateAddress()){
            double jarak = getJarak();
            Toast.makeText(this, "Jarak : "+String.valueOf(jarak), Toast.LENGTH_SHORT).show();
            double ongkir = 0;
            if (jarak >= 2)
                ongkir = Math.round((jarak - 1) * 3000);

            double totalHarga = Math.round(hrg + ongkir);
            ongkir_et.setText(String.valueOf(ongkir));
            total_et.setText(String.valueOf(totalHarga));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OrderLaundryActivity.this, UserMainActivity.class));
        finish();
    }

    @SuppressLint("MissingPermission")
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build()
            );

            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
            locationComponent.setRenderMode(RenderMode.COMPASS);

//            this.origin = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
//                    locationComponent.getLastKnownLocation().getLatitude());

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void initLayers(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage(DESTINATION_ICON_ID,
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_compass_icon));
        GeoJsonSource geoJsonSource = new GeoJsonSource(DESTINATION_SOURCE_ID);
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer(DESTINATION_SYMBOL_LAYER_ID, DESTINATION_SOURCE_ID);
        destinationSymbolLayer.withProperties(
                iconImage(DESTINATION_ICON_ID),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);


        if (originMarker != null) {
            mapboxMap.removeMarker(originMarker);
        }
        //LatLng latLng = new LatLng(origin.latitude(), origin.longitude());
        LatLng latLngToko = new LatLng(tokoPoint.latitude(), tokoPoint.longitude());
        //originMarker = mapboxMap.addMarker(new MarkerOptions().position(latLng));
        tokoMarker = mapboxMap.addMarker(new MarkerOptions().position(latLngToko));


        geocoder = new Geocoder(OrderLaundryActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(origin.latitude(), origin.longitude(), 1);
            changeAlamatET(addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GEOCODER : ", e.getMessage());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Grant Location Permission", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);

                }
            });
        } else {
            Toast.makeText(this, "Permission Not granted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            changeAlamatET(selectedCarmenFeature.placeName());
            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs("geojsonSourceLayerId");
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}
                        ));
                    }
                    LatLng search_destination = new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                            ((Point) selectedCarmenFeature.geometry()).longitude());
                    if (originMarker != null) {
                        mapboxMap.removeMarker(originMarker);
                    }
                    originMarker = mapboxMap.addMarker(new MarkerOptions().position(search_destination));
                    origin = Point.fromLngLat(search_destination.getLongitude(), search_destination.getLatitude());

                    hitungHarga();

                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);

                }
            }
        }
    }

    private void changeAlamatET(String placeName) {
        TextInputEditText alamat_et = findViewById(R.id.alamat_et);
        alamat_et.setText(placeName);
    }

    private boolean validateAddress(){
        TextInputEditText alamat_et = findViewById(R.id.alamat_et);

        if(alamat_et.getText().toString().trim() != null && origin != null)
            return true;
        else
            return false;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        //Feature symbolLayerIconFeatureList = new ArrayList<>();
                        enableLocationComponent(style);
                        initLayers(style);
                    }
                });

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                if (originMarker != null) {
                    mapboxMap.removeMarker(originMarker);
                }
                originMarker = mapboxMap.addMarker(new MarkerOptions().position(point));
                origin = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                geocoder = new Geocoder(OrderLaundryActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(origin.latitude(), origin.longitude(), 1);
                    changeAlamatET(addresses.get(0).getAddressLine(0));
                    hitungHarga();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("GEOCODER : ", e.getMessage());
                }


                return false;
            }
        });

    }
}