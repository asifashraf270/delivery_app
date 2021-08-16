package com.example.khushbakht.grocerjin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.khushbakht.grocerjin.CustomScrollView;
import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;
import com.example.khushbakht.grocerjin.network.NetworkApiListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SuccessfulDelivery extends AppCompatActivity implements  GestureOverlayView.OnGestureListener {
    private SuccessfulLister deliveryLister = new SuccessfulLister();
    private NetworkApiController mNetworkApiController = null;
    private Context context = null;
    Button submit;
    TextView header;
    Toolbar toolbar;
    CustomScrollView scrollView;
    EditText comments, amountPaidET;
    ImageButton refresh;
    File file;
    GestureOverlayView gestureView;
    Bitmap bm;
    String itemSelected;
    String orderId;
    String amountPaid;
    String address;
    String timeOfDelivery;
    TextView orderid;
    TextView amountpaid;
    TextView addressText;
    TextView timeOfdelivery;
    ImageButton clear;
    private ProgressDialog pd;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_delivery);
        setContext(this);
        itemSelected = getIntent().getStringExtra("itemSelected");
        orderId = getIntent().getStringExtra("orderID");
        amountPaid = getIntent().getStringExtra("amountPaid");
        address = getIntent().getStringExtra("address");
        timeOfDelivery = getIntent().getStringExtra("timeOfDelivery");
        comments = (EditText) findViewById(R.id.comments);
        amountPaidET = (EditText) findViewById(R.id.amountPaidET);
        refresh = (ImageButton) findViewById(R.id.refresh);
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessfulDelivery.this, OrderDetail.class);
                startActivity(intent);
                finish();
            }
        });
        clear = (ImageButton) findViewById(R.id.clear);
        scrollView = (CustomScrollView) findViewById(R.id.scrollView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        gestureView = (GestureOverlayView) findViewById(R.id.signaturePad);
        header = (TextView) findViewById(R.id.headerText);
        header.setText("Successful Delivery");
        setSupportActionBar(toolbar);
        orderid = (TextView) findViewById(R.id.orderNo);
        amountpaid = (TextView) findViewById(R.id.amountPaid);
        addressText = (TextView) findViewById(R.id.address);
        timeOfdelivery = (TextView) findViewById(R.id.timeOfDilevery);
        orderid.setText(orderId);
        amountpaid.setText(amountPaid);
        amountPaidET.setText(amountPaid);
        addressText.setText(address);
        gestureView.addOnGestureListener(this);
        String date=timeOfDelivery;
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("MMM dd, yyyy   hh:mm");
        if (newDate!= null) {
            date = spf.format(newDate);
        }
        timeOfdelivery.setText(date);        mNetworkApiController = NetworkApiController.getInstance(getContext());
        submit = (Button) findViewById(R.id.button);
        comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (null != comments.getLayout() && comments.getLayout().getLineCount() > 3) {
                    comments.getText().delete(comments.getText().length() - 1, comments.getText().length());
                }
            }
        });

    }

    public void saveSig(View view) {

        try {

            gestureView.setDrawingCacheEnabled(true);
            bm = Bitmap.createBitmap(gestureView.getDrawingCache());
            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File directory = contextWrapper.getDir(getFilesDir().getName(), Context.MODE_PRIVATE);
            file =  new File(directory,"signature.png");
            file.createNewFile();
            String data = "TEST DATA";
            FileOutputStream fos = new FileOutputStream("signature.png", true); // save
            fos.write(data.getBytes());
            //compress to specified format (PNG), quality - which is ignored for PNG, and out stream
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
//            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

        }
        catch (Exception e) {

            Log.v("Gestures", e.getMessage());

            e.printStackTrace();

        }
    }

    @Override
    public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
        scrollView.setEnableScrolling(false);
    }

    @Override
    public void onGesture(GestureOverlayView overlay, MotionEvent event) {
    }

    @Override
    public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
        scrollView.setEnableScrolling(true);
    }

    @Override
    public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh.setVisibility(View.GONE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSig(v);
                if (orderId!= null && file!=null&& itemSelected!=null) {
                    mNetworkApiController.sendPost(amountPaidET.getText().toString(), orderId, file, comments.getText().toString(), itemSelected);
                    pd = new ProgressDialog(SuccessfulDelivery.this);
                    pd.setMessage("Please wait");
                    pd.show();
                }

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestureView.cancelClearAnimation();
                gestureView.clear(true);
            }
        });


    }

    public void setContext(SuccessfulDelivery context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNetworkApiController.addListener(deliveryLister);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNetworkApiController.removeListener(deliveryLister);
    }

    private class SuccessfulLister extends NetworkApiListener {

        @Override
        public void onResponse(String statusMessage, int statusCode) {
            super.onResponse(statusMessage, statusCode);
            if(pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
            if (statusCode == 200) {
//                mNetworkApiController.changeStatus(orderId, itemSelected);
                dialog = new AlertDialog.Builder(SuccessfulDelivery.this);
                dialog.setMessage(statusMessage);
                dialog.setNegativeButton(Html.fromHtml("<b>"+getString(R.string.ok_button)+"</b>"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                                mNetworkApiController.changeStatus(orderId,itemSelected);
                                mNetworkApiController.userslist();
                                OrderDetail.orderobject = null;
                                Intent intent = new Intent(SuccessfulDelivery.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                dialog.show();

            }

        }
        @Override
        public void onResponseError(String statusMessage) {
            super.onResponseError(statusMessage);

//            if(pd.isShowing()) {
//                pd.dismiss();
//                pd = null;
//            }
            dialog = new AlertDialog.Builder(SuccessfulDelivery.this);
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
    public void onBackPressed() {
        Intent intent = new Intent(this, OrderDetail.class);
        startActivity(intent);
        finish();
    }

}
