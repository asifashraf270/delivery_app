package com.example.khushbakht.grocerjin.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khushbakht.grocerjin.Check_Internet;
import com.example.khushbakht.grocerjin.Checks;
import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.RememberCredentials;
import com.example.khushbakht.grocerjin.classes.GPSTrackerService;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;
import com.example.khushbakht.grocerjin.network.NetworkApiListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static TextInputLayout username, password;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    public GPSTrackerService mService;
    Button login;
    boolean loginFlag = false;
    RememberCredentials rememberCredentials = new RememberCredentials(Login.this);
    double latitude = 0.0, longitude = 0.0;
    String lat, lng;
    ProgressDialog pd;
    boolean mBound = false;
    private LoginLister loginLister = new LoginLister();
    private NetworkApiController mNetworkApiController = null;
    private Context context = null;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sharedPreferences;
    PermissionListener permissionlistenerLocall = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            try {

                displayLocation();
            } catch (Exception exp) {
                Log.i("Helloo...", exp.getLocalizedMessage());
            }


        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

        }

//        @Override
//        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            //Validation.showToastMsg(Section1Activity.this,"you reject permission,you can not use this service");
//        }


    };
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GPSTrackerService.LocalBinder binder = (GPSTrackerService.LocalBinder) service;
            mService = binder.getService();
            if (mService.isGSPOn()) {
                mService.getLocation();


            }
//            timer = new Timer();
//            myTimerTask = new MyTimerTask();
//
//            timer.schedule(myTimerTask, 1000, 5000);5000
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void displayLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                //  Toast.makeText(getLocalContext(),String.valueOf(latitude+" lng "+longitude),Toast.LENGTH_LONG).show();
                lat = String.valueOf(latitude);
                lng = String.valueOf(longitude);

            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public void showSettingsAlert() {

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Login.this);


        //Setting Dialog Title

        alertDialog.setTitle("Alert");


        //Setting Dialog Message

        alertDialog.setMessage("Please turn on your GPS");


        //On Pressing Setting button

        alertDialog.setPositiveButton(Html.fromHtml("<b>" + getString(R.string.turn_on) + "</b>"), new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                startActivity(intent);

            }

        });


        //On pressing cancel button

        alertDialog.setNegativeButton(Html.fromHtml("<b>" + getString(R.string.cancel_button) + "</b>"), new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }

        });


        alertDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setContext(this);
//        settings = getSharedPreferences("mySharedPref", 0);
//        if (settings.getBoolean("connected", false)) {
//        /* The user has already login, so start the dashboard */
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//       }
        username = (TextInputLayout) findViewById(R.id.username);
        password = (TextInputLayout) findViewById(R.id.verification);
        login = (Button) findViewById(R.id.login);
        mNetworkApiController = NetworkApiController.getInstance(getContext());
        if (rememberCredentials.getFlag() == true) {
            username.getEditText().setText(rememberCredentials.getUsername());
            password.getEditText().setText(rememberCredentials.getPassword());

        } else {
            username.getEditText().setText("");
            password.getEditText().setText("");
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, GPSTrackerService.class);

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        callLocalPermission();
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (Check_Internet.getInstance(Login.this).isOnline()) {

                        if (!(username.getEditText().getText().toString().isEmpty() && password.getEditText().getText().toString().isEmpty())) {
                            if (password.getEditText().getText().toString().length() <= 3) {
                                Toast.makeText(Login.this, "Please Enter atleast greater than 3 entities for passsword", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    new Checks().showAlert(Login.this, "No Internet Connection", "Ok");
                }


                if (!mService.isGSPOn()) {
                    showSettingsAlert();
                    return;
                } else {

                    mService.getLocation();
                    mNetworkApiController.login(username.getEditText().getText().toString(), password.getEditText().getText().toString());
                    pd = new ProgressDialog(Login.this);
                    pd.setMessage("Authenticating...");
                    pd.setCancelable(true);
                    pd.show();
                }

//                SharedPreferences.Editor editor = settings.edit();
//                editor.putBoolean("connected", true);
//                editor.commit();


            }
        });


    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNetworkApiController.addListener(loginLister);
    }

//    @Override
//    public void update(Observable o, Object arg) {
//
//        displayLocation();
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNetworkApiController.removeListener(loginLister);
        unbindService(mConnection);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
        unbindService(mConnection);

    }

    private void callLocalPermission() {

        new TedPermission(getContext())
                .setPermissionListener(permissionlistenerLocall)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(

                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE


                )
                .check();
    }

    /**
     * Method to display the location on UI
     */

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();

            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    private class LoginLister extends NetworkApiListener {

        @Override
        public void onResponseLogin(String statusMessage, int statusCode) {
            super.onResponseLogin(statusMessage, statusCode);


            pd.dismiss();
            if (statusCode == 200) {
                loginFlag = true;
                rememberCredentials.setFlag(loginFlag);
                rememberCredentials.setUserName(username.getEditText().getText().toString());
                rememberCredentials.setPassword(password.getEditText().getText().toString());


                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(Login.this, statusMessage, Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                dialog.setMessage(statusMessage);
                dialog.setNegativeButton(Html.fromHtml("<b>" + getString(R.string.ok_button) + "</b>"),
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
            pd.dismiss();
            Toast.makeText(Login.this, statusMessageError, Toast.LENGTH_SHORT).show();
        }
    }

}
