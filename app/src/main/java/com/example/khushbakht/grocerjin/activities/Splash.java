package com.example.khushbakht.grocerjin.activities;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.RememberCredentials;

public class Splash extends AppCompatActivity {


    Handler mHandler;
    RememberCredentials remember = new RememberCredentials(Splash.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        ContactListManager.getContactListManager().destroy();
        mHandler = new Handler();
        onServiceReady();


    }
    private void onServiceReady(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                if(remember.getFlag()==true)
//                {
//                   Login.username.setText(remember.getUsername());
//                   Login.password.setText(remember.getPassword());
//                }
//                else {
//                    Login.username.setText("");
//                    Login.password.setText("");
//                }
                Intent i = new Intent(Splash.this,Login.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }
}

