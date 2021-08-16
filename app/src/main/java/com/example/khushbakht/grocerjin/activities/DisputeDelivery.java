package com.example.khushbakht.grocerjin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.audio.ui.ViewProxy;
import com.example.khushbakht.grocerjin.controller.NetworkApiController;
import com.example.khushbakht.grocerjin.network.NetworkApiListener;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class DisputeDelivery extends AppCompatActivity {
    private MediaRecorder recorder;

    private DisputedLister disputedLister = new DisputedLister();
    private NetworkApiController mNetworkApiController = null;
    private Context context = null;

    private static final String TAG = "RecordActivity";
    String filePath;
    String itemSelected;
    String orderId;
    String amountPaid;
    File file;
    String address;
    String timeOfDelivery;
    TextView orderid;
    TextView amountpaid;
    TextView addressText;
    TextView timeOfdelivery;
    private Toolbar toolbar;
    private TextView recordTimeText;
    private ImageButton audioSendButton;
    private View recordPanel;
    private View slideText;
    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Timer timer;
    TextView header;
    EditText comments;
    Button disputedSubmit;
    ImageButton refresh;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute_delivery);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setVisibility(View.GONE);
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisputeDelivery.this, OrderDetail.class);
                startActivity(intent);
                finish();
            }
        });
        itemSelected = getIntent().getStringExtra("itemSelected");
        orderId = getIntent().getStringExtra("orderID");
        amountPaid = getIntent().getStringExtra("amountPaid");
        address = getIntent().getStringExtra("address");
        timeOfDelivery = getIntent().getStringExtra("timeOfDelivery");
        mNetworkApiController = NetworkApiController.getInstance(getContext());
        header = (TextView) findViewById(R.id.headerText);
        header.setText("Disputed Delivery");
        comments = (EditText) findViewById(R.id.comments);
        disputedSubmit = (Button) findViewById(R.id.disputedbutton);
        orderid = (TextView) findViewById(R.id.orderNo);
        amountpaid = (TextView) findViewById(R.id.amountPaid);
        addressText = (TextView) findViewById(R.id.address);
        timeOfdelivery = (TextView) findViewById(R.id.timeOfDilevery);
        orderid.setText(orderId);
        amountpaid.setText(amountPaid);
        addressText.setText(address);
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
        timeOfdelivery.setText(date);

        recordPanel = findViewById(R.id.record_panel);
        recordTimeText = (TextView) findViewById(R.id.recording_time_text);
        slideText = findViewById(R.id.slideText);
        audioSendButton = (ImageButton) findViewById(R.id.chat_audio_send_button);
        TextView textView = (TextView) findViewById(R.id.slideToCancelTextView);
        textView.setText("Slide to cancel");
        audioSendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    params.leftMargin = dp(30);
                    slideText.setLayoutParams(params);
                    ViewProxy.setAlpha(slideText, 1);
                    startedDraggingX = -1;
                    recordTimeText.setText("00:00");
                    // startRecording();
                    try {
                        startrecord();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    audioSendButton.getParent()
                            .requestDisallowInterceptTouchEvent(true);
                    recordPanel.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    startedDraggingX = -1;
                    try {
                        stoprecord();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // stopRecording(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = motionEvent.getX();
                    if (x < -distCanMove) {

                           file = null;

                            if (timer != null) {
                                timer.cancel();
                            }
                            recordTimeText.setText("00:00");

                        // stopRecording(false);
                    }
                    x = x + ViewProxy.getX(audioSendButton);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    if (startedDraggingX != -1) {
                        float dist = (x - startedDraggingX);
                        params.leftMargin = dp(30) + (int) dist;
                        slideText.setLayoutParams(params);
                        float alpha = 1.0f + dist / distCanMove;
                        if (alpha > 1) {
                            alpha = 1;
                        } else if (alpha < 0) {
                            alpha = 0;
                        }
                        ViewProxy.setAlpha(slideText, alpha);
                    }
                    if (x <= ViewProxy.getX(slideText) + slideText.getWidth()
                            + dp(30)) {
                        if (startedDraggingX == -1) {
                            startedDraggingX = x;
                            distCanMove = (recordPanel.getMeasuredWidth()
                                    - slideText.getMeasuredWidth() - dp(48)) / 2.0f;
                            if (distCanMove <= 0) {
                                distCanMove = dp(80);
                            } else if (distCanMove > dp(80)) {
                                distCanMove = dp(80);
                            }
                        }
                    }
                    if (params.leftMargin > dp(30)) {
                        params.leftMargin = dp(30);
                        slideText.setLayoutParams(params);
                        ViewProxy.setAlpha(slideText, 1);
                        startedDraggingX = -1;
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });

    }

    String filepath;

    private String getFilename()
    {
        filepath = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".mp3";

        file = new File(filepath);

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return (file.getAbsolutePath() );
    }

    public void startRecording() throws IOException {
        if (recorder == null) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            recorder.setOutputFile(getFilename()); // This is my file path to store data
            recorder.prepare();
            recorder.start();
        }

        // make sure the directory we plan to store the recording in exists

    }


    ProgressDialog pd;
    @Override
    protected void onStart() {
        super.onStart();

        itemSelected = getIntent().getStringExtra("itemSelected");
        disputedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderId!= null && file!=null && itemSelected!=null) {
                    mNetworkApiController.disputedCase(orderId, file, comments.getText().toString(), itemSelected);
                    pd = new ProgressDialog(DisputeDelivery.this);
                    pd.setMessage("Please wait");
                    pd.show();
                }
                if (orderId!= null && file==null && itemSelected!=null) {
                    mNetworkApiController.undelivered(orderId, comments.getText().toString(), itemSelected);
                    pd = new ProgressDialog(DisputeDelivery.this);
                    pd.setMessage("Please wait");
                    pd.show();
                }



//                if (comments.getText().toString().isEmpty())
//                {
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(DisputeDelivery.this);
//                    dialog.setMessage("Please add comments.");
//                    dialog.setPositiveButton(Html.fromHtml("<b>"+getString(R.string.ok_button)+"</b>"),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });dialog.show();
//                }



            }
        });


    }

    private void startrecord() throws IOException {


        // TODO Auto-generated method stub
        startTime = SystemClock.uptimeMillis();
        timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1000);
        vibrate();
        startRecording();
        Log.i("Imran", "Shafi");
    }
    public void stopRecording() throws IllegalStateException {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    private void stoprecord() throws IOException {
        // TODO Auto-generated method stub

        if (timer != null) {
            timer.cancel();
        }
        if (recordTimeText.getText().toString().equals("00:00")) {
            return;
        }
        vibrate();
        stopRecording();


    }


    private void vibrate() {
        // TODO Auto-generated method stub
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dp(float value) {
        return (int) Math.ceil(1 * value);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            final String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(updatedTime)),
                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(updatedTime)));
            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                    .toMinutes(updatedTime));
            System.out.println(lastsec + " hms " + hms);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (recordTimeText != null)
                            recordTimeText.setText(hms);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
        }
    }

    public void setContext(DisputeDelivery context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNetworkApiController.addListener(disputedLister);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNetworkApiController.removeListener(disputedLister);
    }

    private class DisputedLister extends NetworkApiListener {

        @Override
        public void onResponse(String statusMessage, int statusCode) {
            super.onResponse(statusMessage, statusCode);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            if(statusCode == 200)
            {
                dialog = new AlertDialog.Builder(DisputeDelivery.this);
                dialog.setMessage(statusMessage);
                dialog.setNegativeButton(Html.fromHtml("<b>"+getString(R.string.ok_button)+"</b>"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                                mNetworkApiController.changeStatus(orderId,itemSelected);
                                mNetworkApiController.userslist();
                                Intent intent = new Intent(DisputeDelivery.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                dialog.show();

            }


        }

        @Override
        public void onResponseError(String statusMessageError) {
            super.onResponseError(statusMessageError);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            dialog = new AlertDialog.Builder(DisputeDelivery.this);
            dialog.setMessage(statusMessageError);
            dialog.setNegativeButton(Html.fromHtml("<b>"+getString(R.string.ok_button)+"</b>"),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
//                                mNetworkApiController.changeStatus(orderId,itemSelected);
                        }
                    });
            dialog.show();        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OrderDetail.class);
        startActivity(intent);
        finish();
    }


}
