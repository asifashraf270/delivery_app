package com.example.khushbakht.grocerjin.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.View;
import android.widget.*;
import android.widget.Button;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.classes.GPSTrackerService;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;
import com.example.khushbakht.grocerjin.model.OrderList.OrderList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class OrderDetail extends AppCompatActivity implements OnMapReadyCallback {

    private static final float POLYLINE_STROKE_WIDTH_PX = 5;
    private Spinner spinner;
    private Toolbar toolbar;
    TextView header;
    Button itemView;
    LatLng destination;
    LatLng origin;
    android.widget.Button submit;
    String _globalString;
    TextView timer;
    TextView orderNo;
    TextView address;
    TextView paidMoney;
    TextView phoneNo;
    ImageButton refresh, callCustomer;
    Double lat, lng;
    Calendar yourDate = Calendar.getInstance();
    Calendar now = Calendar.getInstance();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private static final String USERS_LIST = "users_list";

    private static final String FORMAT = "%02d:%02d:%02d";

    long millis;
    private String[] items = new String[]{"", "Disputed", "Delivered", "Undelivered"};

    public static OrderList orderobject = null;
    TextView totalitems;
    GPSTrackerService gpsTrackerService = new GPSTrackerService();
    private Double currentlat;
    private Double currentlng;
    private String orderID, totalAmount, timeOfDelivery, totalItems, addressToPass, phoneNumber, lats, lngs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }


        header = (TextView) findViewById(R.id.headerText);
        header.setText("Order Detail");
        timer = (TextView) findViewById(R.id.timer);
        itemView = (Button) findViewById(R.id.itemView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        phoneNo = (TextView) findViewById(R.id.phoneNo);
        orderNo = (TextView) findViewById(R.id.orderNo);
        address = (TextView) findViewById(R.id.address);
        refresh = (ImageButton) findViewById(R.id.refresh);
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetail.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        callCustomer = (ImageButton) findViewById(R.id.callCustomer);
        totalitems = (TextView) findViewById(R.id.totalItems);
        paidMoney = (TextView) findViewById(R.id.paidMoney);
        startService(new Intent(this, GPSTrackerService.class));


        setSupportActionBar(toolbar);
        spinner = (Spinner) findViewById(R.id.spinner);


    }


    @Override
    protected void onStart() {
        super.onStart();
        orderobject = NetworkApiController.dataConroller.getOrderList();
        if (orderobject == null)
            orderobject = new OrderList();
        orderID = orderobject.getOrderno();
        totalAmount = orderobject.getAmount();
        addressToPass = orderobject.getAddress();
        timeOfDelivery = orderobject.getDateTime();
        phoneNumber = orderobject.getPhone_number();
        totalItems = orderobject.getTotalItems();
        lats = orderobject.getLatitude();
        lngs = orderobject.getLongitude();
        if (!lats.equalsIgnoreCase("") && !lngs.equalsIgnoreCase("")) {
            lat = Double.valueOf(lats);
            lng = Double.valueOf(lngs);
        }
        phoneNo.setText(phoneNumber);
        orderNo.setText(orderID);
        address.setText(addressToPass);
        paidMoney.setText("Rs:" + totalAmount);
        totalitems.setText(totalItems);
        submit = (Button) findViewById(R.id.submit);
        String originalString = orderobject.getDateTime();
        if (originalString != null && originalString.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(originalString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                yourDate.setTimeInMillis(date.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        refresh.setVisibility(View.GONE);
        callCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + orderobject.getPhone_number()));
                if (ActivityCompat.checkSelfPermission(OrderDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                finish();
            }
        });

        millis = yourDate.getTimeInMillis() - now.getTimeInMillis();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(OrderDetail.this,
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                _globalString = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_globalString == "") {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(OrderDetail.this);
                    dialog.setMessage("Please select the order status");

                    dialog.setNegativeButton(Html.fromHtml("<b>" + getString(R.string.ok_button) + "</b>"),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                }

                if (_globalString == "Delivered") {
                    Intent intent = new Intent(getApplicationContext(), SuccessfulDelivery.class);
                    intent.putExtra("itemSelected", _globalString);
                    intent.putExtra("phoneNo", phoneNumber);
                    intent.putExtra("orderID", orderID);
                    intent.putExtra("amountPaid", totalAmount);
                    intent.putExtra("address", addressToPass);
                    intent.putExtra("timeOfDelivery", timeOfDelivery);
                    startActivity(intent);
                }
                if (_globalString == "Disputed") {
                    Intent intent = new Intent(getApplicationContext(), DisputeDelivery.class);
                    intent.putExtra("itemSelected", _globalString);
                    intent.putExtra("phoneNo", phoneNumber);
                    intent.putExtra("orderID", orderID);
                    intent.putExtra("amountPaid", totalAmount);
                    intent.putExtra("address", addressToPass);
                    intent.putExtra("timeOfDelivery", timeOfDelivery);
                    startActivity(intent);
                }

                if (_globalString == "Undelivered") {
                    Intent intent = new Intent(getApplicationContext(), UndeliveredOrder.class);
                    intent.putExtra("itemSelected", _globalString);
                    intent.putExtra("phoneNo", phoneNumber);
                    intent.putExtra("orderID", orderID);
                    intent.putExtra("amountPaid", totalAmount);
                    intent.putExtra("address", addressToPass);
                    intent.putExtra("timeOfDelivery", timeOfDelivery);
                    startActivity(intent);
                }
            }
        });


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetail.this, ItemView.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);

            }
        });
        new CountDownTimer(millis, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                timer.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("time's up!");
            }
        }.start();


        //changeFragmentFragment(MapsFragment.getInstance(lat, lng));


    }

    /*public void changeFragmentFragment(Fragment pFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map, pFragment, pFragment.getClass().getName());
        transaction.commit();

    }*/

    @Override
    public void onMapReady(GoogleMap map) {
        //gpsTrackerService.getLocation();
//        origin = new LatLng(currentlat, currentlng);
        if (lat != null && lng != null) {
            destination = new LatLng(lat, lng);


            map.addMarker(new MarkerOptions().position(destination));
//            map.addMarker(new MarkerOptions().position(origin));
            //Polyline polyline = map.addPolyline(new PolylineOptions().clickable(true).add(origin).add(destination));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }

//        polyline.setColor(Color.RED);
//        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
//DO WHATEVER YOU WANT WITH GOOGLEMAP
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    private void initilizeMap() {
        MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapview));
        mapFragment.getMapAsync(this);

    }

    /**
     * Method to verify google play services on the device
     */

    @Override
    public void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}