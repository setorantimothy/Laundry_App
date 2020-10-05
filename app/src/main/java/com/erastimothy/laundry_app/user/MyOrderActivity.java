package com.erastimothy.laundry_app.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.adapter.LaundryRecyclerViewAdapter;
import com.erastimothy.laundry_app.dao.LaundryDao;
import com.erastimothy.laundry_app.dao.UserDao;
import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.model.User;
import com.erastimothy.laundry_app.preferences.LaundryPreferences;
import com.erastimothy.laundry_app.preferences.UserPreferences;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private SearchView searchView;
    private LaundryRecyclerViewAdapter adapter;
    private MaterialButton btnBack;
    private LaundryDao laundryDao;
    private LaundryPreferences laundryPreferences;
    private List<Laundry> myLaundryList;
    private List<Laundry> laundryListFull;
    private Laundry laundry;
    private UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        myLaundryList = new ArrayList<>();
        userDao = new UserDao(this);

        btnBack = findViewById(R.id.btnBack);
        searchView = (SearchView) findViewById(R.id.search_pegawai);
        refreshLayout = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.pegawai_rv);

        //asign data
        laundryDao = new LaundryDao(this);
        laundryDao.setAllDataLaundry();

        laundryPreferences = new LaundryPreferences(this);
        laundryListFull =  laundryPreferences.getListLaundryFromSharedPreferences();


        //assign data only my order not all
        for(int i=0 ; i < laundryListFull.size(); i++){
            if(laundryListFull.get(i).getUid().trim().equalsIgnoreCase(userDao.getCurrentUid().trim())){
                myLaundryList.add((Laundry) laundryListFull.get(i));
            }
        }

        //Toast.makeText(this, myLaundryList.get(0).getOrder_id(), Toast.LENGTH_SHORT).show();

        adapter = new LaundryRecyclerViewAdapter(this,myLaundryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOrderActivity.super.onBackPressed();

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
            }
        });

    }


}