package com.example.khushbakht.grocerjin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;
import com.example.khushbakht.grocerjin.network.NetworkApiListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UndeliveredOrder extends AppCompatActivity {

    private UndeliveredLister undeliveredLister = new UndeliveredLister();
    private NetworkApiController mNetworkApiController = null;
    private Context context = null;
    TextView header;
    Toolbar toolbar;
    EditText comments;
    Button submit;
    String itemSelected;
    String orderId;
    String amountPaid;
    String address;
    String timeOfDelivery;
    TextView orderid;
    TextView amountpaid;
    TextView addressText;
    TextView timeOfdelivery;
    ImageButton refresh;
    private ProgressDialog pd;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undelivered_order);
        setContext(this);
        itemSelected = getIntent().getStringExtra("itemSelected");
        orderId = getIntent().getStringExtra("orderID");
        amountPaid = getIntent().getStringExtra("amountPaid");
        address = getIntent().getStringExtra("address");
        timeOfDelivery = getIntent().getStringExtra("timeOfDelivery");
        comments = (EditText) findViewById(R.id.undeliveredcomments);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        header = (TextView) findViewById(R.id.headerText);
        header.setText("Undelivered");
        setSupportActionBar(toolbar);
        orderid = (TextView) findViewById(R.id.orderNo);
        amountpaid = (TextView) findViewById(R.id.amountPaid);
        addressText = (TextView) findViewById(R.id.address);
        refresh = (ImageButton) findViewById(R.id.refresh);
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UndeliveredOrder.this, OrderDetail.class);
                startActivity(intent);
                finish();
            }
        });
        timeOfdelivery = (TextView) findViewById(R.id.timeOfDilevery);
        orderid.setText(orderId);
        amountpaid.setText(amountPaid);
        addressText.setText(address);
        comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (null != comments.getLayout() && comments.getLayout().getLineCount() > 3) {
                    comments.getText().delete(comments.getText().length() - 1, comments.getText().length());
                }
            }
        });
        String date = timeOfDelivery;
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("MMM dd, yyyy   hh:mm");
        if (newDate != null) {
            date = spf.format(newDate);
        }
        timeOfdelivery.setText(date);
        mNetworkApiController = NetworkApiController.getInstance(getContext());
        submit = (Button) findViewById(R.id.undelivered);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        refresh.setVisibility(View.GONE);

        itemSelected = getIntent().getStringExtra("itemSelected");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderId != null) {

                    mNetworkApiController.undelivered(orderId, comments.getText().toString(), itemSelected);
                    pd = new ProgressDialog(UndeliveredOrder.this);
                    pd.setMessage("Please wait");
                    pd.show();
                }

            }
        });
    }

    public void setContext(UndeliveredOrder context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNetworkApiController.addListener(undeliveredLister);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNetworkApiController.removeListener(undeliveredLister);
    }

    private class UndeliveredLister extends NetworkApiListener {

        @Override
        public void onResponse(String statusMessage, int statusCode) {
            super.onResponse(statusMessage, statusCode);
            pd.dismiss();

            if (statusCode == 200) {
                dialog = new AlertDialog.Builder(UndeliveredOrder.this);
                dialog.setMessage(statusMessage);
                dialog.setNegativeButton(Html.fromHtml("<b>" + getString(R.string.ok_button) + "</b>"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                                mNetworkApiController.changeStatus(orderId,itemSelected);
                                mNetworkApiController.userslist();
                                Intent intent = new Intent(UndeliveredOrder.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                dialog.show();

            } else {
                dialog = new AlertDialog.Builder(UndeliveredOrder.this);
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
            Toast.makeText(getContext(), statusMessageError, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OrderDetail.class);
        startActivity(intent);
        finish();
    }
}
