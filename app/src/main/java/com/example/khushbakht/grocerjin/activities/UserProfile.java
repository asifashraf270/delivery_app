package com.example.khushbakht.grocerjin.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;
import com.example.khushbakht.grocerjin.network.NetworkApiListener;

public class UserProfile extends AppCompatActivity {
    TextView profilename, profileusername, profilelocation, header;
    private ProfileLister profileLister = new ProfileLister();
    private NetworkApiController mNetworkApiController = null;
    private Context context = null;
    Toolbar toolbar;
    String latitude, longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setVisibility(View.GONE);
        profilename = (TextView) findViewById(R.id.profilename);
        header = (TextView) findViewById(R.id.headerText);
        header.setText("Profile");
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        profileusername = (TextView) findViewById(R.id.profileUsername);
        profilelocation = (TextView) findViewById(R.id.profileLocation);
        mNetworkApiController = NetworkApiController.getInstance(getContext());

    }
    public void setContext(Context context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }


    @Override
    public void onStart() {
        super.onStart();
        mNetworkApiController.profileInfo();
        profilelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserProfile.this, UserLocation.class);
                intent.putExtra("lat", latitude);
                intent.putExtra("lng", longitude);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mNetworkApiController.addListener(profileLister);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNetworkApiController.removeListener(profileLister);
    }

    private class ProfileLister extends NetworkApiListener {

        @Override
        public void onResponseProfile(String statusMessage, int code, String name, String username, String location, String lat, String lng) {
            super.onResponseProfile(statusMessage, code, name, username, location, lat, lng);
            if (code == 200) {
                profilename.setText(name);
                profileusername.setText(username);
                profilelocation.setText(location);
                latitude = lat;
                longitude = lng;
            }
            else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfile.this);
                dialog.setMessage(statusMessage);
                dialog.setNegativeButton(Html.fromHtml("<b>"+getString(R.string.ok_button)+"</b>"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }



        }

        @Override
        public void onResponseError(String statusMessageError) {
            super.onResponseError(statusMessageError);
            Toast.makeText(getContext(), statusMessageError, Toast.LENGTH_SHORT).show();
        }
    }
}
