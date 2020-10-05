package com.erastimothy.laundry_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.erastimothy.laundry_app.admin.AdminMainActivity;
import com.erastimothy.laundry_app.dao.TokoDao;
import com.erastimothy.laundry_app.preferences.UserPreferences;
import com.erastimothy.laundry_app.dao.UserDao;
import com.erastimothy.laundry_app.model.User;
import com.erastimothy.laundry_app.user.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDao = new UserDao(this);
        Intent intents = getIntent();
        Bundle bundle = intents.getExtras();
        if(bundle !=null)
            changeUI();
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeUI();
                }
            },3000);
        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "Channel 1";
            CharSequence name = "Channel 1";
            String description = "This is Channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String mag = "Successful";
                        if(!task.isSuccessful()){
                            mag = "Failed";
                        }
                        //check message push notification
                        Log.i("Main Activity","PUSH NOTIFICATION : "+mag);
                    }
        });

    }

    void changeUI(){
        //cek login
        UserPreferences sessionUser = new UserPreferences(MainActivity.this);
        User user = sessionUser.getUserLoginFromSharedPrefernces();

        //if user exist, auto login , go to homepage
        if(user.getName() != null){
            TokoDao tokoDao = new TokoDao(MainActivity.this);
            tokoDao.setTokoFromDatabase();
            Toast.makeText(MainActivity.this, "Welcome back "+user.getName(), Toast.LENGTH_SHORT).show();
            if(user.is_owner()){
                //owner redirect to owner page
                Intent intent = new Intent(MainActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(MainActivity.this, UserMainActivity.class);
                startActivity(intent);
            }

        } else { // if user doesnt exist, go to login page
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}