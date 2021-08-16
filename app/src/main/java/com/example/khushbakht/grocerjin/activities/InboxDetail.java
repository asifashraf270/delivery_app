package com.example.khushbakht.grocerjin.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InboxDetail extends AppCompatActivity {

    private String inboxMessage = null;
    private String inboxTime = null;

    TextView tvInboxMessage;
    TextView tvInboxTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView header = (TextView) findViewById(R.id.headerText);
        ImageButton refresh = (ImageButton) findViewById(R.id.refresh);
        header.setText("Notifications Detail");
        refresh.setVisibility(View.GONE);

        tvInboxMessage = (TextView) findViewById(R.id.tvInboxDetailMessage);

        tvInboxTime = (TextView) findViewById(R.id.tvInboxDetailTime);

        inboxMessage = NetworkApiController.dataConroller.getInboxMessage();
        inboxTime = NetworkApiController.dataConroller.getInboxTime();

        if (inboxMessage != null)
        {
            tvInboxMessage.setText(inboxMessage);
        }

        if (inboxTime != null)
        {
            String date=inboxTime;
            SimpleDateFormat spf=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date newDate= null;
            try {
                newDate = spf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf= new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa");
            if (newDate!= null) {
                date = spf.format(newDate);
            }
            tvInboxTime.setText(date);
        }

    }
}
