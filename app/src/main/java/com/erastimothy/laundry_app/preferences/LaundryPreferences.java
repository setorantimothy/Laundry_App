package com.erastimothy.laundry_app.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.model.Toko;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class LaundryPreferences {
    SharedPreferences laundySP;
    SharedPreferences.Editor editor;
    Context context;
    Laundry laundry;

    public static final String KEY_LAUNDRY = "laundry";
    public static final String KEY_ALLLAUNDRY = "all-laundry";
    public static final String KEY_LAUNDRYINFO = "all-laundry-info";

    public LaundryPreferences(Context context) {
        this.context = context;
        laundySP = context.getSharedPreferences("laundryPreferences",Context.MODE_PRIVATE);
        editor = laundySP.edit();
    }

    public void createLaundry(Laundry laundry){
        Gson gson = new Gson();
        String json = gson.toJson(laundry);
        editor.putString(KEY_LAUNDRY,json);

        editor.commit();
    }

    public void setLaundryInfo(float pendapatan, int totalPesanan){
        editor.putFloat(KEY_LAUNDRYINFO,pendapatan);
        editor.putInt(KEY_LAUNDRYINFO,totalPesanan);

        editor.commit();
    }

    public void setAllLaundry(List<Laundry> laundryList){
        Gson gson = new Gson();
        String json = gson.toJson(laundryList);

        editor.putString(KEY_ALLLAUNDRY,json);
        editor.commit();
    }

    public List<Laundry> getListLaundryFromSharedPreferences(){
        List<Laundry> laundryList;
        String json = laundySP.getString(KEY_ALLLAUNDRY,null);
        if(json != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<Laundry>>(){}.getType();
            laundryList = gson.fromJson(json, type);
            return laundryList;
        }
        return null;
    }

}
