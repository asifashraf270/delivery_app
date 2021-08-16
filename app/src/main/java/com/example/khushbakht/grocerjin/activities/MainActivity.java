package com.example.khushbakht.grocerjin.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;

import com.example.khushbakht.grocerjin.AppConstant;
import com.google.android.material.navigation.NavigationView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.RememberCredentials;
import com.example.khushbakht.grocerjin.callbacks.SimpleItemTouchHelperCallback;
import com.example.khushbakht.grocerjin.adapters.CustomAdapter;
import com.example.khushbakht.grocerjin.adapters.ItemTouchHelperAdapter;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;
import com.example.khushbakht.grocerjin.model.OrderList.OrderList;
import com.example.khushbakht.grocerjin.network.NetworkApiListener;
import com.example.khushbakht.grocerjin.listeners.OnItemClickListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivity.LoginLister loginLister = new MainActivity.LoginLister();
    private NetworkApiController mNetworkApiController = null;
    RememberCredentials rememberCredentials = new RememberCredentials(MainActivity.this);
    private Context context = null;
    public static final String TAG = MainActivity.class.getCanonicalName();
    private static String USERS_LIST = "users_list";
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    ItemTouchHelperAdapter mAdapter;
    public ImageButton refresh;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    EditText email, password;
    String order_id;
    AlertDialog alertDialog;
    AlertDialog.Builder exitDialog;
    public static int _code;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ProgressBar progressBar;


    private PushReicever pushReciver;
    private SharedPreferences sharedPreferences;

    public MainActivity() {

    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView header = (TextView) findViewById(R.id.headerText);
        header.setText("Order List");
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNetworkApiController = NetworkApiController.getInstance(context);
        refresh = (ImageButton) findViewById(R.id.refresh);

        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setVisibility(View.VISIBLE);
        back.setImageResource(R.drawable.navigationicon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        if (pushReciver == null) {
            IntentFilter filter = new IntentFilter("push");
            pushReciver = new PushReicever();

            LocalBroadcastManager.getInstance(this).registerReceiver(pushReciver, filter);
        }
        sharedPreferences = getSharedPreferences(RememberCredentials.SharedPreferenceName, MODE_PRIVATE);
        saveFirebaseToken();

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.toolbarmenu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // action with ID action_refresh was selected
//            case R.id.action_refresh:
//
//                Intent intent = getIntent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                finish();
//                startActivity(intent);
//                break;
//            // action with ID action_settings was selected
//            default:
//                break;
//        }
//
//        return true;
//    }

    class PushReicever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Intent intent11 = getIntent();
            intent11.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            startActivity(intent11);
            finish();

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mNetworkApiController.userslist();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNetworkApiController.userslist();
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);


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
    public void onBackPressed() {
        //additional code
        exitDialog = new AlertDialog.Builder(MainActivity.this);
        exitDialog.setTitle("Exit");
        exitDialog.setMessage("Do you want to exit ?");
        exitDialog.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                           /* Intent intent = new Intent(MainActivity.this, Login.class);
                            startActivity(intent);*/
                        finish();
                    }
                });
        exitDialog.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        exitDialog.show();


    }


    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        mNetworkApiController.addListener(loginLister);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.myOrders:
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent);

                        return true;

                    // For rest of the options just show a toast on click

                    case R.id.notifications:
                        Intent intent1 = new Intent(MainActivity.this, Notifications.class);
                        startActivity(intent1);
                        return true;
                    case R.id.profile:
                        Intent intent2 = new Intent(MainActivity.this, UserProfile.class);
                        startActivity(intent2);
                        return true;
                    case R.id.logout:
                        rememberCredentials.setFlag(false);
                        Intent in = new Intent(MainActivity.this, Login.class);
                        startActivity(in);
                        finish();


                    default:
                        return true;

                }
            }

            //Check to see which item was being clicked and perform appropriate action
        });
       /* ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();*/


    }

    @Override
    public void onDestroy() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(pushReciver);
        super.onDestroy();
        mNetworkApiController.removeListener(loginLister);

    }


    private class LoginLister extends NetworkApiListener {

        @Override
        public void onResponse(final String statusMessage) {
            super.onResponse(statusMessage);
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(statusMessage);
            dialog.setPositiveButton(Html.fromHtml("<b>" + getString(R.string.ok_button) + "</b>"),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();

        }

        @Override
        public void onPickResponse(String message, int code) {
            super.onPickResponse(message, code);
            progressBar.setVisibility(View.GONE);
            if (code == 200) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("Order picked!");
                dialog.setPositiveButton(Html.fromHtml("<b>" + getString(R.string.ok_button) + "</b>"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage(message);
                dialog.setPositiveButton(Html.fromHtml("<b>" + getString(R.string.ok_button) + "</b>"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
            mNetworkApiController.userslist();


        }

        @Override
        public void onResponseError(final String statusMessageError) {
            super.onResponseError(statusMessageError);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    //     Toast.makeText(getContext(), statusMessageError , Toast.LENGTH_SHORT).show();

                }
            });

        }

        @Override
        public void onResponse(final List<OrderList> userListResponses) {
            progressBar.setVisibility(View.GONE);
            adapter = new CustomAdapter(userListResponses, getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ItemTouchHelper.Callback callback =
                    new SimpleItemTouchHelperCallback(adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    OrderList order = userListResponses.get(position);
                    NetworkApiController.dataConroller.setOrderList(order);
                    Intent intent = new Intent(MainActivity.this, OrderDetail.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onPickCick(View view, int position) {
                    final OrderList orderList = userListResponses.get(position);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setMessage("Pick this order for delivery ?");
                    dialog.setPositiveButton(Html.fromHtml("<b>" + getString(R.string.yes) + "</b>"),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mNetworkApiController.pickOrder(orderList.getOrderno());
                                }
                            });
                    dialog.setNegativeButton(Html.fromHtml("<b>" + getString(R.string.no) + "</b>"),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                }
            });


        }
    }


    private void saveFirebaseToken() {
        if (sharedPreferences.getString(AppConstant.FIREBASETOKEN, "").isEmpty()) {
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.e("response", token);
            mNetworkApiController.saveFirebaseToken(token);
        } else {
            Log.e("response", sharedPreferences.getString(AppConstant.FIREBASETOKEN, ""));
        }
    }


}
