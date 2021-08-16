package com.example.khushbakht.grocerjin.activities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.adapters.NotifyAdapter;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;
import com.example.khushbakht.grocerjin.listeners.OnItemClickListener;
import com.example.khushbakht.grocerjin.sugarDB.InboxTable;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {

    List<InboxTable> cm ;
    InboxTable inboxTable = new InboxTable();


    private RecyclerView recyclerView;
    private NotifyAdapter inboxAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private TextView nullNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView header = (TextView) findViewById(R.id.headerText);
        ImageButton refresh = (ImageButton) findViewById(R.id.refresh);
        header.setText("Notifications");
        refresh.setVisibility(View.GONE);
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notifications.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });




        nullNotifications = (TextView) findViewById(R.id.null_notifications);
        recyclerView = (RecyclerView) findViewById(R.id.inbox_recyclerView);





    }

    @Override
    protected void onResume() {
        super.onResume();


        if (inboxTable.listAll(InboxTable.class) == null || inboxTable.listAll(InboxTable.class).size() == 0) {


            recyclerView.setVisibility(View.GONE);
            nullNotifications.setVisibility(View.VISIBLE);


        }



        if (InboxTable.listAll(InboxTable.class)!=null && InboxTable.listAll(InboxTable.class).size()>0)
        {

            recyclerView.setVisibility(View.VISIBLE);
            nullNotifications.setVisibility(View.GONE);

            cm = InboxTable.listAll(InboxTable.class);

            final List<InboxTable> inboxTableList = new ArrayList<>(cm);

            inboxAdapter = new NotifyAdapter(inboxTableList,Notifications.this);

            layoutManager = new LinearLayoutManager(Notifications.this);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(inboxAdapter);
            inboxAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                        Intent intent = new Intent(Notifications.this, InboxDetail.class);
                        NetworkApiController.dataConroller.setInboxMessage(InboxTable.listAll(InboxTable.class).get(position).getNotifyMesssage());
                        NetworkApiController.dataConroller.setInboxTime(InboxTable.listAll(InboxTable.class).get(position).getNotifyTime());

                    InboxTable inboxTable = InboxTable.findById(InboxTable.class,inboxTableList.get(position).getId());
                    inboxTable.setReadFlag(true);
                    inboxTable.save();


                        startActivity(intent);




                }

                @Override
                public void onPickCick(View view, int position) {

                }
            });


        }

    }

}
