package com.erastimothy.laundry_app.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.erastimothy.laundry_app.Model.User;
import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.databinding.ActivityMyAccountBinding;
import com.google.android.material.button.MaterialButton;

public class MyAccountActivity extends AppCompatActivity {
    User user;
    ActivityMyAccountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);


        Bundle bundle = getIntent().getExtras();

        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_account);
        user = new User(bundle.getString("uid"),bundle.getString("name"),bundle.getString("email"),bundle.getString("password"),bundle.getString("phoneNumber"),bundle.getBoolean("_owner"));
        binding.emailEt.setText(user.getEmail());
        binding.nameEt.setText(user.getName());
        binding.phoneNumberEt.setText(user.getPhoneNumber());
        //binding.setUserData(user);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyAccountActivity.this,UserMainActivity.class));
        finish();
    }
}