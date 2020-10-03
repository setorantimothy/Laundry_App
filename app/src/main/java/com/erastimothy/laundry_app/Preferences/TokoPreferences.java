package com.erastimothy.laundry_app.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.erastimothy.laundry_app.Model.Toko;
import com.erastimothy.laundry_app.Model.User;

public class TokoPreferences {
    SharedPreferences tokoSP;
    SharedPreferences.Editor editor;
    Context context;
    Toko toko;

    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_NAME = "name";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitute";
    public static final String KEY_TELP = "telp";

    public TokoPreferences(Context context) {
        this.context = context;
        tokoSP = context.getSharedPreferences("tokoPreferences",Context.MODE_PRIVATE);
        editor = tokoSP.edit();
    }

    public void createToko(String name,String alamat, Double longitute, Double latitute,String telp){
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_ALAMAT,alamat);
        editor.putString(KEY_LONGITUDE,String.valueOf(longitute));
        editor.putString(KEY_LATITUDE,String.valueOf(latitute));
        editor.putString(KEY_TELP,String.valueOf(telp));

        editor.commit();
    }

    public Toko getToko(){
        String nama,alamat,telp;
        Double longitute,latitute;
        nama = tokoSP.getString(KEY_NAME,null);
        alamat = tokoSP.getString(KEY_ALAMAT,null);
        telp = tokoSP.getString(KEY_TELP,null);
        longitute = Double.parseDouble(tokoSP.getString(KEY_LONGITUDE,null));
        latitute = Double.parseDouble(tokoSP.getString(KEY_LATITUDE,null));

        Toko toko = new Toko(alamat,nama,telp,longitute,latitute);
        Log.i("TOKO : ",toko.getAlamat());
        return toko;
    }
}
