package com.erastimothy.laundry_app.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.erastimothy.laundry_app.dao.UserDao;
import com.erastimothy.laundry_app.model.User;
import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.preferences.UserPreferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserMainActivity extends AppCompatActivity {
    private UserDao userDao;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        userDao = new UserDao(this);

        //get root database
        database = FirebaseDatabase.getInstance();
        //set table
        reference = database.getReference("users");

        RelativeLayout signOutMenu = (RelativeLayout) findViewById(R.id.signOutMenu);
        RelativeLayout myAccountMenu = (RelativeLayout) findViewById(R.id.myAccountMenu);
        RelativeLayout orderMenu = (RelativeLayout) findViewById(R.id.orderMenu);
        RelativeLayout myOrderMenu = (RelativeLayout) findViewById(R.id.myOrderMenu);

        orderMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, OrderLaundryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myOrderMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, MyOrderActivity.class);
                startActivity(intent);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.UserMainActivityLayout,new MyOrderFragment())
//                        .commit();
            }
        });

        signOutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserPreferences sessionUser = new UserPreferences(UserMainActivity.this);
                Toast.makeText(UserMainActivity.this, "Bye, " + sessionUser.getUserLoginFromSharedPrefernces().getName(), Toast.LENGTH_SHORT).show();
                sessionUser.logout();
                userDao.signOut();
            }
        });

        myAccountMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserPreferences sessionUser = new UserPreferences(UserMainActivity.this);
                User user = sessionUser.getUserLoginFromSharedPrefernces();

                Bundle bundle = new Bundle();
                bundle.putString("uid", user.getUid());
                bundle.putString("name", user.getName());
                bundle.putString("email", user.getEmail());
                bundle.putString("password", user.getPassword());
                bundle.putString("phoneNumber", user.getPhoneNumber());
                bundle.putBoolean("_owner", user.is_owner);
                Intent intent = new Intent(UserMainActivity.this, MyAccountActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }
        });
    }
}