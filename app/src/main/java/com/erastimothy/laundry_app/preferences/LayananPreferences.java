package com.erastimothy.laundry_app.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.erastimothy.laundry_app.adapter.LayananAdapter;
import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.model.Layanan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class LayananPreferences {
    SharedPreferences layananSP;
    SharedPreferences.Editor editor;
    Context context;
    Layanan layanan;

    public static final String KEY_LAYANAN = "layanan";
    public static final String KEY_ALLLAYANAN = "all-layanan";
    public static final String KEY_MAX_ID = "max-id-layanan";

    public LayananPreferences(Context context) {
        this.context = context;
        layananSP = context.getSharedPreferences("layananPreferences",Context.MODE_PRIVATE);
        editor = layananSP.edit();
    }

    public void createLayanan(Layanan layanan){
        Gson gson = new Gson();
        String json = gson.toJson(layanan);
        editor.putString(KEY_LAYANAN,json);

        editor.commit();
    }

    public void setAllLayanan(List<Layanan> layananList){
        Gson gson = new Gson();
        String json = gson.toJson(layananList);
        editor.remove(KEY_LAYANAN);
        editor.putString(KEY_LAYANAN,json);
        editor.commit();
    }

    public void setLayananMaxId(int id){
        editor.remove(KEY_MAX_ID);
        editor.putInt(KEY_MAX_ID,id);
        editor.commit();
    }

    public int getLayananMaxId(){
        return layananSP.getInt(KEY_MAX_ID,0);
    }

    public List<Layanan> getListLayananFromSharedPreferences(){
        List<Layanan> layananList;
        String json = layananSP.getString(KEY_LAYANAN,null);
        if(json != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<Layanan>>(){}.getType();
            layananList = gson.fromJson(json, type);
            return layananList;
        }
        return null;
    }
}
