package com.erastimothy.laundry_app.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.adapter.LayananAdapter;
import com.erastimothy.laundry_app.adapter.RiwayatOrderanLaundryAdapter;
import com.erastimothy.laundry_app.dao.LaundryDao;
import com.erastimothy.laundry_app.dao.LayananDao;
import com.erastimothy.laundry_app.dao.UserDao;
import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.model.Layanan;
import com.erastimothy.laundry_app.preferences.LaundryPreferences;
import com.erastimothy.laundry_app.preferences.LayananPreferences;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class LayananActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private SearchView searchView;
    private LayananAdapter adapter;
    private MaterialButton btnBack,btnAdd;
    private LayananDao layananDao;
    private LayananPreferences layananPreferences;
    private List<Layanan> layananList;
    private Layanan layanan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan);

        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        searchView = (SearchView) findViewById(R.id.search_pegawai);
        refreshLayout = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.order_rv);

        //asign data
        setData();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayananActivity.super.onBackPressed();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LayananActivity.this, LayananFormActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                refreshLayout.setRefreshing(false);
                return false;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    public void setData(){
        layananDao = new LayananDao(this);
        layananDao.setAllDataLayanan();

        layananPreferences = new LayananPreferences(this);
        layananList =  layananPreferences.getListLayananFromSharedPreferences();

        adapter = new LayananAdapter(this,layananList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}