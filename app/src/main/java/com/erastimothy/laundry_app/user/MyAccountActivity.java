package com.erastimothy.laundry_app.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.erastimothy.laundry_app.model.User;
import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.databinding.ActivityMyAccountBinding;

public class MyAccountActivity extends AppCompatActivity {
    User user;
    ActivityMyAccountBinding binding;
    String nama,phoneNumber;
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

//    public void updateUser(View view){
//        TextInputEditText name_et = view.findViewById(R.id.nama_et);
//        TextInputEditText phoneNumber_et = view.findViewById(R.id.phoneNumber_et);
//
//        String _uid = user.getUid();
//        String _name = name_et.getText().toString().trim();
//        String _phoneNumber = phoneNumber_et.getText().toString().trim();
//        user.setName(_name);
//        user.setPhoneNumber(_phoneNumber);
//        UserDao userDao = new UserDao(MyAccountActivity.this);
//        userDao.updateUser(user,_uid);
//        Toast.makeText(MyAccountActivity.this, "Data Berhasil diubah !", Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyAccountActivity.this,UserMainActivity.class));
        finish();
    }
}