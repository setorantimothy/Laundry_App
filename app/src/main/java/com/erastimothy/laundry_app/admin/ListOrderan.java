package com.erastimothy.laundry_app.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.erastimothy.laundry_app.CameraScannerFragment;
import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.adapter.LaundryRecyclerViewAdapter;
import com.erastimothy.laundry_app.adapter.OrderanLaundryAdapter;
import com.erastimothy.laundry_app.dao.LaundryDao;
import com.erastimothy.laundry_app.dao.UserDao;
import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.preferences.LaundryPreferences;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ListOrderan extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private SearchView searchView;
    private OrderanLaundryAdapter adapter;
    private MaterialButton btnBack,btnCamera;
    private LaundryDao laundryDao;
    private LaundryPreferences laundryPreferences;
    private List<Laundry> myLaundryList;
    private List<Laundry> laundryListFull;
    private Laundry laundry;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orderan);
        userDao = new UserDao(this);

        btnBack = findViewById(R.id.btnBack);
        btnCamera = findViewById(R.id.btnCamera);
        searchView = (SearchView) findViewById(R.id.search_pegawai);
        refreshLayout = findViewById(R.id.swipe_refresh);

        getData();

        btnCamera.setOnClickListener(new View.OnClickListener() {
            CameraScannerFragment cameraScannerFragment = new CameraScannerFragment();
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.list_orderan_layout, cameraScannerFragment)
                        .commit();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOrderan.this,AdminMainActivity.class));
                finish();
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
                refreshLayout.setRefreshing(false);
                getData();
            }
        });

    }

    private void getData(){
        myLaundryList = new ArrayList<>();

        recyclerView = findViewById(R.id.order_rv);

        //asign data
        laundryDao = new LaundryDao(this);
        laundryDao.setAllDataLaundry();

        laundryPreferences = new LaundryPreferences(this);
        laundryListFull =  laundryPreferences.getListLaundryFromSharedPreferences();


        //assign data only my order not all
        for(int i=0 ; i < laundryListFull.size(); i++){
            if(!laundryListFull.get(i).getStatus().equalsIgnoreCase("Pesanan Selesai") && !laundryListFull.get(i).getStatus().equalsIgnoreCase("Pesanan Batal")){
                myLaundryList.add((Laundry) laundryListFull.get(i));
            }
        }


        adapter = new OrderanLaundryAdapter(this,myLaundryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ListOrderan.this,AdminMainActivity.class));
        finish();
    }
}