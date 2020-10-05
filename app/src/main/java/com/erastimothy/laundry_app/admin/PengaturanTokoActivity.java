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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.erastimothy.laundry_app.dao.TokoDao;
import com.erastimothy.laundry_app.model.Toko;
import com.erastimothy.laundry_app.preferences.TokoPreferences;
import com.erastimothy.laundry_app.R;
import com.google.android.material.textfield.TextInputEditText;
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

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import java.util.List;
import java.util.Locale;

public class PengaturanTokoActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {
    private TokoPreferences tokoSP;
    private Toko toko;
    private TokoDao tokoDao;


    private static final String DESTINATION_SYMBOL_LAYER_ID = "destination-symbol-layer-id";
    private static final String DESTINATION_ICON_ID = "destination-icon-id";
    private static final String DESTINATION_SOURCE_ID = "destination-source-id";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private Point origin;
    private Marker originMarker;

    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_pengaturan_toko);
        tokoSP = new TokoPreferences(this);
        toko = tokoSP.getToko();
        tokoDao = new TokoDao(this);


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        TextInputEditText namaToko_et = findViewById(R.id.namaToko_et);
        TextInputEditText alamatToko_et = findViewById(R.id.alamat_et);
        TextInputEditText telp_et = findViewById(R.id.telp_et);
        Button btnSave = findViewById(R.id.btnSave);


        if (toko.getName().trim() != null) {
            namaToko_et.setText(toko.getName());
            alamatToko_et.setText(toko.getAlamat());
            telp_et.setText(toko.getTelp());
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toko _toko = new Toko(alamatToko_et.getText().toString(), namaToko_et.getText().toString(), telp_et.getText().toString(), origin.longitude(), origin.latitude());
                tokoDao.saveToko(_toko);
                Toast.makeText(PengaturanTokoActivity.this, "Berhasil menyimpan perubahan!", Toast.LENGTH_SHORT).show();
            }
        });
        alamatToko_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(PengaturanTokoActivity.this, "before : " + origin.latitude(), Toast.LENGTH_SHORT).show();
                    Intent intent = new PlaceAutocomplete.IntentBuilder()
                            .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                            .placeOptions(PlaceOptions.builder()
                                    .backgroundColor(Color.parseColor("#EEEEEE"))
                                    .limit(10)
                                    .build(PlaceOptions.MODE_CARDS))
                            .build(PengaturanTokoActivity.this);
                    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
                }
            }
        });


    }


    @SuppressLint("MissingPermission")
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build()
            );

            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);

            if (this.toko.getLatitude() != null) {
                this.origin = Point.fromLngLat(this.toko.getLongitude(), this.toko.getLatitude());
            } else {
                this.origin = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                        locationComponent.getLastKnownLocation().getLatitude());
            }
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
        LatLng latLng = new LatLng(origin.latitude(), origin.longitude());
        originMarker = mapboxMap.addMarker(new MarkerOptions().position(latLng));
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
                geocoder = new Geocoder(PengaturanTokoActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(origin.latitude(), origin.longitude(), 1);
                    changeAlamatET(addresses.get(0).getAddressLine(0));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("GEOCODER : ", e.getMessage());
                }


                return false;
            }
        });

    }
}