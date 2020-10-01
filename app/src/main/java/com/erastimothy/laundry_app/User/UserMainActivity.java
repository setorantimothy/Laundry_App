package com.erastimothy.laundry_app.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.erastimothy.laundry_app.Dao.UserDao;
import com.erastimothy.laundry_app.LoginActivity;
import com.erastimothy.laundry_app.R;
import com.google.android.material.button.MaterialButton;

public class UserMainActivity extends AppCompatActivity {
    private UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        userDao = new UserDao(this);
        RelativeLayout signOutMenu = (RelativeLayout) findViewById(R.id.signOutMenu);

        signOutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserMainActivity.this, "Bye, "+userDao.getCurrentUid().toString(), Toast.LENGTH_SHORT).show();
                userDao.signOut();
            }
        });
    }
}