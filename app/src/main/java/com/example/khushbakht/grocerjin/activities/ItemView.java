package com.example.khushbakht.grocerjin.activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.callbacks.SimpleItemTouchHelperCallback;
import com.example.khushbakht.grocerjin.adapters.ItemTouchHelperAdapter;
import com.example.khushbakht.grocerjin.adapters.ItemViewAdapter;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;
import com.example.khushbakht.grocerjin.model.productList.Product;
import com.example.khushbakht.grocerjin.network.NetworkApiListener;

import java.util.ArrayList;
import java.util.List;

public class ItemView extends AppCompatActivity {
    ItemLister loginLister = new ItemLister();

    ArrayList<Product> data= new ArrayList<>();
    private NetworkApiController mNetworkApiController = null;
    private Context context = null;
    public static final String TAG = MainActivity.class.getCanonicalName();
    private static String USERS_LIST = "users_list";
    private RecyclerView recyclerView;
    private ItemViewAdapter adapter;
    ItemTouchHelperAdapter mAdapter;
    private Toolbar toolbar;
    TextView amount, delivery_charges, total;
    ImageButton refresh;
    String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        refresh = (ImageButton) findViewById(R.id.refresh);
        amount = (TextView) findViewById(R.id.orderAmount);
        total = (TextView) findViewById(R.id.totalAmount);
        delivery_charges = (TextView) findViewById(R.id.deliveryCharges);
        setSupportActionBar(toolbar);
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemView.this, OrderDetail.class);
                startActivity(intent);
                finish();
            }
        });

        orderId = getIntent().getStringExtra("orderID");


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    @Override
    public void onStart() {
        super.onStart();

        refresh.setVisibility(View.GONE);
        amount.setText("Rs: "+NetworkApiController.dataConroller.getOrderList().get_amount());
        total.setText("Rs: "+NetworkApiController.dataConroller.getOrderList().getAmount());
        delivery_charges.setText("Rs: "+NetworkApiController.dataConroller.getOrderList().getDelivery_charges());
        mNetworkApiController = NetworkApiController.getInstance(context);
        mNetworkApiController.getItems(orderId);

    }

    public Context getContext()
    {
        return context;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }


    @Override
    public void onResume() {
        super.onResume();
        mNetworkApiController.addListener(loginLister);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        mNetworkApiController.removeListener(loginLister);

    }


    private class ItemLister extends NetworkApiListener{

        @Override
        public void onResponseError(String statusMessageError) {
            super.onResponseError(statusMessageError);
            Toast.makeText(ItemView.this,statusMessageError, Toast.LENGTH_SHORT).show();

        }


        @Override
        public void onItemResponse(List<Product> itemListResponses) {
            super.onItemResponse(itemListResponses);


            adapter = new ItemViewAdapter(context,itemListResponses);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);

        }
    }


}
