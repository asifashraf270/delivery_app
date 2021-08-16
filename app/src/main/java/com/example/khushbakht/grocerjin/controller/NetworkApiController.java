package com.example.khushbakht.grocerjin.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Process;
import android.util.Log;

import com.example.khushbakht.grocerjin.AppConstant;
import com.example.khushbakht.grocerjin.Controller;
import com.example.khushbakht.grocerjin.RememberCredentials;
import com.example.khushbakht.grocerjin.adapters.CustomAdapter;
import com.example.khushbakht.grocerjin.classes.DataPass;
import com.example.khushbakht.grocerjin.firebase.SaveFirebaseTokenModel;
import com.example.khushbakht.grocerjin.model.Postdelivery.DeliveryStatus;
import com.example.khushbakht.grocerjin.model.Postdelivery.DeliveryStatusResponse;
import com.example.khushbakht.grocerjin.model.StatusUpdate.Change_Order_Status;
import com.example.khushbakht.grocerjin.model.login.PostSignUp;
import com.example.khushbakht.grocerjin.model.Postdelivery.Post;
import com.example.khushbakht.grocerjin.model.login.Status;
import com.example.khushbakht.grocerjin.model.OrderList.UserListRequest;
import com.example.khushbakht.grocerjin.model.OrderList.UserListResponse;
import com.example.khushbakht.grocerjin.model.login.response.LoginData;
import com.example.khushbakht.grocerjin.model.pickOrder.PickModel;
import com.example.khushbakht.grocerjin.model.pickOrder.PickResponse;
import com.example.khushbakht.grocerjin.model.pickOrder.PickStatus;
import com.example.khushbakht.grocerjin.model.productList.ProductList;
import com.example.khushbakht.grocerjin.model.productList.ProductStatus;
import com.example.khushbakht.grocerjin.model.productList.ProductsResponse;
import com.example.khushbakht.grocerjin.model.productList.RequestOrderId;
import com.example.khushbakht.grocerjin.model.profile.ProfileRequest;
import com.example.khushbakht.grocerjin.model.profile.ProfileResponse;
import com.example.khushbakht.grocerjin.network.NetworkApiListener;
import com.example.khushbakht.grocerjin.model.login.response.StatusResponse;
import com.example.khushbakht.grocerjin.network.NetworkClient;
import com.example.khushbakht.grocerjin.utility.HelperMethod;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Khushbakht on 8/10/2017.
 */

public class NetworkApiController implements Runnable {

    private static final String TAG = NetworkApiController.class.getCanonicalName();

    public static DataPass dataPass = new DataPass();
    public static Controller dataConroller = new Controller();


    protected ArrayList<UserListResponse> data;
    protected ProfileResponse profileResponse;
    protected ArrayList<ProductList> list;
    CustomAdapter adapter;
    private static NetworkApiController inst = null;
    private Context mContext = null;
    private Thread mThread = null;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private NetworkClient mNetworkClient = null;
    // private DataBaseController mDataBaseController = null;
    private Set<NetworkApiListener> mListeners = new CopyOnWriteArraySet<NetworkApiListener>();
    private SharedPreferences sharedPreferences;


    /**
     * @param {@link Context}
     */
    private NetworkApiController(Context pContext) {
        mContext = pContext;
        mNetworkClient = NetworkClient.getClient();
        mThread = new Thread(this);
        mThread.setName("WebController");
        mThread.start();
        //   mDataBaseController = DataBaseController.getInstance(mContext);
    }

    /**
     * Gets or creates the singleton instance of ApiController.
     *
     * @param {@link Context}
     * @return ApiController
     */
    public synchronized static NetworkApiController getInstance(
            Context pContext) {
        if (inst == null) {
            inst = new NetworkApiController(pContext);
        }
        return inst;
    }


    @Override
    public void run() {
        try {

            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        } catch (Exception e) {
//            LogUtil.error(TAG,
//                    "Error in WebController run and message is ="
//                            + e.getMessage());
        }
    }

    /**
     * This method is used to add the listener
     *
     * @param listener {@link NetworkApiListener}
     */
    public void addListener(NetworkApiListener listener) {
        mListeners.add(listener);
    }

    /**
     * This method is used to remove the register listener
     *
     * @param listener {@link NetworkApiListener}
     */
    public void removeListener(NetworkApiListener listener) {
        mListeners.remove(listener);
    }


    /***
     *
     */

    public synchronized void tempThread() {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    LoginData loginData;

    public synchronized void login(final String userName, final String password) {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                PostSignUp postSignUp = new PostSignUp();
                postSignUp.setEmail(userName);
                postSignUp.setPassword(password);
                sharedPreferences = mContext.getSharedPreferences(RememberCredentials.SharedPreferenceName, Context.MODE_PRIVATE);
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (HelperMethod.convertJavaToJson(postSignUp).toString()));


                Call<LoginData> call = mNetworkClient.getNetworkWebService().login(body);
                call.enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                        if (response.isSuccessful() && response != null) {
                            loginData = response.body();
                            Status status = loginData.getStatus();


                            if (status.getCode() == 200) {
                                if (loginData.getResponse().getLocation_name() != null) {
                                    sharedPreferences.edit().putString(AppConstant.DRIVERID, loginData.getResponse().getEId()).commit();
//                                    FirebaseMessaging.getInstance().subscribeToTopic(loginData.getResponse().getPush_name());
                                }
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponseLogin(status.getMessage(), status.getCode());
                                }
                            } else {
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponseLogin(status.getMessage(), status.getCode());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginData> call, Throwable t) {
                        for (NetworkApiListener l : mListeners) {
                            l.onResponseError(t.getMessage());
                        }
                    }
                });

            }
        });
    }

    public synchronized void saveFirebaseToken(final String token) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = mContext.getSharedPreferences(RememberCredentials.SharedPreferenceName, Context.MODE_PRIVATE);
                SaveFirebaseTokenModel model = new SaveFirebaseTokenModel();
                model.deviceToken = token;
                model.driver_id = Integer.parseInt(sharedPreferences.getString(AppConstant.DRIVERID, "0"));
                Call<ResponseBody> responseBodyCall = mNetworkClient.getNetworkWebService().saveToken(model, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNDc0MDksImRhdGEiOlt7ImV2X2NoYXJnaW5nX3NpdGVfc3RhZmZfaWQiOjEsImV2X2NoYXJnaW5nX3NpdGVfaWQiOjEsInVzZXJfYWNjb3VudF9pZCI6Miwic3RhcnRfZGF0ZV90aW1lIjoiMjAyMC0wNy0wMVQwMDowMDowMC4wMDBaIiwiZW5kX2RhdGVfdGltZSI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInVzZXJBY2NvdW50Ijp7InVzZXJfYWNjb3VudF9pZCI6MiwidXNlcl9uYW1lIjoiam9oYW5zdGF0aW9ubWdyIiwiZW1haWxfYWRkcmVzcyI6ImpvaGFuQGFiYy5jb20iLCJwZXJzb25faWQiOjIsInBhc3N3b3JkIjoiQCNAQEAjIiwidXNlcl9yb2xlX2NvZGUiOiJTTSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJtb2JpbGVfcGhvbmVfbnVtYmVyIjpudWxsLCJwZXJzb24iOnsicGVyc29uX2lkIjoyLCJmaXJzdF9uYW1lIjoiSm9oYW4iLCJsYXN0X25hbWUiOiJTdGF0aW9uTWFuYWdlciIsInRpdGxlX2NvZGUiOiJNUiIsImRhdGVfb2ZfYmlydGgiOiIxOTc0LTAxLTAxIiwic2V4X2NvZGUiOiJNIiwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInNleFJlZiI6eyJzZXhfY29kZSI6Ik0iLCJuYW1lIjoiTWFsZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfSwicGVyc29uQWRkcmVzcyI6eyJwZXJzb25fYWRkcmVzc19pZCI6MiwiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwicGVyc29uX2lkIjoyLCJhZGRyZXNzX2lkIjoxLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbCwiYWRkcmVzc1R5cGVSZWYiOnsiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwibmFtZSI6Ik9mZmljZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfX19LCJ1c2VyUm9sZVJlZiI6eyJ1c2VyX3JvbGVfY29kZSI6IlNNIiwibmFtZSI6IlN0YXRpb24gTWFuYWdlciIsImNyZWF0aW9uX2RhdGVfdGltZSI6IjIwMjAtMDctMDFUMDA6MDA6MDAuMDAwWiIsInBlcm1pc3Npb24iOiI_Pz8_IiwidXNlcl9yb2xlX2dyb3VwX2NvZGUiOiJDUyIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJ1c2VyUm9sZUdyb3VwUmVmIjp7InVzZXJfcm9sZV9ncm91cF9jb2RlIjoiQ1MiLCJuYW1lIjoiQ2hhcmdpbmcgU2l0ZSIsImVuZHBvaW50IjoiYXV0aGVudGljYXRlX2V2X2NoYXJnaW5nX3NpdGVfc3RhZmYiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH19fSwidXNlY2FzZXNQZXJtaXR0ZWQiOlt7InVzZXJfcGVybWlzc2lvbl91c2VjYXNlX2lkIjoxLCJuYW1lIjoiU2V0dXAgUHJpY2VzIiwiZGVzY3JpcHRpb24iOiJNYWtlIGNoYW5nZXMgdG8gQ2hhcmdlIEl0ZXNtIiwidXJsX3BhdGgiOiIvY2hhcmdlX2l0ZW0iLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH0seyJ1c2VyX3Blcm1pc3Npb25fdXNlY2FzZV9pZCI6MiwibmFtZSI6IlNldHVwIFBsYW5zL0J1bmRsZXMiLCJkZXNjcmlwdGlvbiI6Ik1ha2UgY2hhbmdlcyB0byBQbGFucyBhbmQgQnVuZGxlcyIsInVybF9wYXRoIjoiL2J1bmRsZV9wcmljZXMiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH1dLCJldkNoYXJnaW5nU2l0ZXMiOlt7ImV2X2NoYXJnaW5nX3NpdGVfaWQiOjEsIm5hbWUiOiJDaGFyZ2luZyBTaXRlIFBsYXphIiwiZGVzY3JpcHRpb24iOiJDaGFyZ2luZyBTaXRlIFBsYXphIGFuZCBTaG9wcGluZyBNYWxsIiwiZ3BzX2xhdGl0dWRlIjoiNTkuMzI2MTQyIiwiZ3BzX2xvbmdpdHVkZSI6IjE3Ljk4MjA1MyIsImRlZmF1bHRfY3VycmVuY3lfaWQiOjEsInZhdF9pZCI6MSwiYWRkcmVzc19pZCI6MiwiZXZfY2hhcmdpbmdfc2l0ZV9ncm91cF9pZCI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGx9XX0seyJldl9jaGFyZ2luZ19zaXRlX3N0YWZmX2lkIjoyLCJldl9jaGFyZ2luZ19zaXRlX2lkIjoxMjksInVzZXJfYWNjb3VudF9pZCI6Miwic3RhcnRfZGF0ZV90aW1lIjoiMjAyMC0wNy0wMVQwMDowMDowMC4wMDBaIiwiZW5kX2RhdGVfdGltZSI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInVzZXJBY2NvdW50Ijp7InVzZXJfYWNjb3VudF9pZCI6MiwidXNlcl9uYW1lIjoiam9oYW5zdGF0aW9ubWdyIiwiZW1haWxfYWRkcmVzcyI6ImpvaGFuQGFiYy5jb20iLCJwZXJzb25faWQiOjIsInBhc3N3b3JkIjoiQCNAQEAjIiwidXNlcl9yb2xlX2NvZGUiOiJTTSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJtb2JpbGVfcGhvbmVfbnVtYmVyIjpudWxsLCJwZXJzb24iOnsicGVyc29uX2lkIjoyLCJmaXJzdF9uYW1lIjoiSm9oYW4iLCJsYXN0X25hbWUiOiJTdGF0aW9uTWFuYWdlciIsInRpdGxlX2NvZGUiOiJNUiIsImRhdGVfb2ZfYmlydGgiOiIxOTc0LTAxLTAxIiwic2V4X2NvZGUiOiJNIiwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInNleFJlZiI6eyJzZXhfY29kZSI6Ik0iLCJuYW1lIjoiTWFsZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfSwicGVyc29uQWRkcmVzcyI6eyJwZXJzb25fYWRkcmVzc19pZCI6MiwiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwicGVyc29uX2lkIjoyLCJhZGRyZXNzX2lkIjoxLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbCwiYWRkcmVzc1R5cGVSZWYiOnsiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwibmFtZSI6Ik9mZmljZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfX19LCJ1c2VyUm9sZVJlZiI6eyJ1c2VyX3JvbGVfY29kZSI6IlNNIiwibmFtZSI6IlN0YXRpb24gTWFuYWdlciIsImNyZWF0aW9uX2RhdGVfdGltZSI6IjIwMjAtMDctMDFUMDA6MDA6MDAuMDAwWiIsInBlcm1pc3Npb24iOiI_Pz8_IiwidXNlcl9yb2xlX2dyb3VwX2NvZGUiOiJDUyIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJ1c2VyUm9sZUdyb3VwUmVmIjp7InVzZXJfcm9sZV9ncm91cF9jb2RlIjoiQ1MiLCJuYW1lIjoiQ2hhcmdpbmcgU2l0ZSIsImVuZHBvaW50IjoiYXV0aGVudGljYXRlX2V2X2NoYXJnaW5nX3NpdGVfc3RhZmYiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH19fSwidXNlY2FzZXNQZXJtaXR0ZWQiOlt7InVzZXJfcGVybWlzc2lvbl91c2VjYXNlX2lkIjoxLCJuYW1lIjoiU2V0dXAgUHJpY2VzIiwiZGVzY3JpcHRpb24iOiJNYWtlIGNoYW5nZXMgdG8gQ2hhcmdlIEl0ZXNtIiwidXJsX3BhdGgiOiIvY2hhcmdlX2l0ZW0iLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH0seyJ1c2VyX3Blcm1pc3Npb25fdXNlY2FzZV9pZCI6MiwibmFtZSI6IlNldHVwIFBsYW5zL0J1bmRsZXMiLCJkZXNjcmlwdGlvbiI6Ik1ha2UgY2hhbmdlcyB0byBQbGFucyBhbmQgQnVuZGxlcyIsInVybF9wYXRoIjoiL2J1bmRsZV9wcmljZXMiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH1dLCJldkNoYXJnaW5nU2l0ZXMiOlt7ImV2X2NoYXJnaW5nX3NpdGVfaWQiOjEyOSwibmFtZSI6InRlc3Qgc3RhdGlvbiIsImRlc2NyaXB0aW9uIjoiMzQgc2RzZHNkIiwiZ3BzX2xhdGl0dWRlIjoiMzEuNDkwMzA5NjIiLCJncHNfbG9uZ2l0dWRlIjoiNzQuNDAxNjQ1NjYiLCJkZWZhdWx0X2N1cnJlbmN5X2lkIjoxLCJ2YXRfaWQiOjEsImFkZHJlc3NfaWQiOjEzMCwiZXZfY2hhcmdpbmdfc2l0ZV9ncm91cF9pZCI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGx9XX1dLCJpYXQiOjE1OTc4MTE0MDl9.koNeFYpWWV07_QvQTd_QcfDam7q9LUpHi_dPP3BqWUY");
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            try {
                                String resp = response.body().string();
                                JSONObject object = new JSONObject(resp);
                                if (object.getInt("statusCode") == 200) {
                                    sharedPreferences.edit().putString(AppConstant.FIREBASETOKEN, token).commit();
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                sharedPreferences.edit().remove(AppConstant.FIREBASETOKEN).commit();
                            }
                        } else {
                            sharedPreferences.edit().remove(AppConstant.FIREBASETOKEN).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        sharedPreferences.edit().remove(AppConstant.FIREBASETOKEN).commit();
                    }
                });

            }
        });
    }


    public synchronized ArrayList<UserListResponse> userslist() {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {


                final UserListRequest userListRequest = new UserListRequest();
                if (loginData != null) {
                    userListRequest.setAdmin_id(loginData.getResponse().getEId());
                    userListRequest.setLocation_id(loginData.getResponse().getLocation_id());
                }

                Call<UserListResponse> call = mNetworkClient.getNetworkWebService().orderList("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNDc0MDksImRhdGEiOlt7ImV2X2NoYXJnaW5nX3NpdGVfc3RhZmZfaWQiOjEsImV2X2NoYXJnaW5nX3NpdGVfaWQiOjEsInVzZXJfYWNjb3VudF9pZCI6Miwic3RhcnRfZGF0ZV90aW1lIjoiMjAyMC0wNy0wMVQwMDowMDowMC4wMDBaIiwiZW5kX2RhdGVfdGltZSI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInVzZXJBY2NvdW50Ijp7InVzZXJfYWNjb3VudF9pZCI6MiwidXNlcl9uYW1lIjoiam9oYW5zdGF0aW9ubWdyIiwiZW1haWxfYWRkcmVzcyI6ImpvaGFuQGFiYy5jb20iLCJwZXJzb25faWQiOjIsInBhc3N3b3JkIjoiQCNAQEAjIiwidXNlcl9yb2xlX2NvZGUiOiJTTSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJtb2JpbGVfcGhvbmVfbnVtYmVyIjpudWxsLCJwZXJzb24iOnsicGVyc29uX2lkIjoyLCJmaXJzdF9uYW1lIjoiSm9oYW4iLCJsYXN0X25hbWUiOiJTdGF0aW9uTWFuYWdlciIsInRpdGxlX2NvZGUiOiJNUiIsImRhdGVfb2ZfYmlydGgiOiIxOTc0LTAxLTAxIiwic2V4X2NvZGUiOiJNIiwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInNleFJlZiI6eyJzZXhfY29kZSI6Ik0iLCJuYW1lIjoiTWFsZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfSwicGVyc29uQWRkcmVzcyI6eyJwZXJzb25fYWRkcmVzc19pZCI6MiwiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwicGVyc29uX2lkIjoyLCJhZGRyZXNzX2lkIjoxLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbCwiYWRkcmVzc1R5cGVSZWYiOnsiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwibmFtZSI6Ik9mZmljZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfX19LCJ1c2VyUm9sZVJlZiI6eyJ1c2VyX3JvbGVfY29kZSI6IlNNIiwibmFtZSI6IlN0YXRpb24gTWFuYWdlciIsImNyZWF0aW9uX2RhdGVfdGltZSI6IjIwMjAtMDctMDFUMDA6MDA6MDAuMDAwWiIsInBlcm1pc3Npb24iOiI_Pz8_IiwidXNlcl9yb2xlX2dyb3VwX2NvZGUiOiJDUyIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJ1c2VyUm9sZUdyb3VwUmVmIjp7InVzZXJfcm9sZV9ncm91cF9jb2RlIjoiQ1MiLCJuYW1lIjoiQ2hhcmdpbmcgU2l0ZSIsImVuZHBvaW50IjoiYXV0aGVudGljYXRlX2V2X2NoYXJnaW5nX3NpdGVfc3RhZmYiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH19fSwidXNlY2FzZXNQZXJtaXR0ZWQiOlt7InVzZXJfcGVybWlzc2lvbl91c2VjYXNlX2lkIjoxLCJuYW1lIjoiU2V0dXAgUHJpY2VzIiwiZGVzY3JpcHRpb24iOiJNYWtlIGNoYW5nZXMgdG8gQ2hhcmdlIEl0ZXNtIiwidXJsX3BhdGgiOiIvY2hhcmdlX2l0ZW0iLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH0seyJ1c2VyX3Blcm1pc3Npb25fdXNlY2FzZV9pZCI6MiwibmFtZSI6IlNldHVwIFBsYW5zL0J1bmRsZXMiLCJkZXNjcmlwdGlvbiI6Ik1ha2UgY2hhbmdlcyB0byBQbGFucyBhbmQgQnVuZGxlcyIsInVybF9wYXRoIjoiL2J1bmRsZV9wcmljZXMiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH1dLCJldkNoYXJnaW5nU2l0ZXMiOlt7ImV2X2NoYXJnaW5nX3NpdGVfaWQiOjEsIm5hbWUiOiJDaGFyZ2luZyBTaXRlIFBsYXphIiwiZGVzY3JpcHRpb24iOiJDaGFyZ2luZyBTaXRlIFBsYXphIGFuZCBTaG9wcGluZyBNYWxsIiwiZ3BzX2xhdGl0dWRlIjoiNTkuMzI2MTQyIiwiZ3BzX2xvbmdpdHVkZSI6IjE3Ljk4MjA1MyIsImRlZmF1bHRfY3VycmVuY3lfaWQiOjEsInZhdF9pZCI6MSwiYWRkcmVzc19pZCI6MiwiZXZfY2hhcmdpbmdfc2l0ZV9ncm91cF9pZCI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGx9XX0seyJldl9jaGFyZ2luZ19zaXRlX3N0YWZmX2lkIjoyLCJldl9jaGFyZ2luZ19zaXRlX2lkIjoxMjksInVzZXJfYWNjb3VudF9pZCI6Miwic3RhcnRfZGF0ZV90aW1lIjoiMjAyMC0wNy0wMVQwMDowMDowMC4wMDBaIiwiZW5kX2RhdGVfdGltZSI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInVzZXJBY2NvdW50Ijp7InVzZXJfYWNjb3VudF9pZCI6MiwidXNlcl9uYW1lIjoiam9oYW5zdGF0aW9ubWdyIiwiZW1haWxfYWRkcmVzcyI6ImpvaGFuQGFiYy5jb20iLCJwZXJzb25faWQiOjIsInBhc3N3b3JkIjoiQCNAQEAjIiwidXNlcl9yb2xlX2NvZGUiOiJTTSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJtb2JpbGVfcGhvbmVfbnVtYmVyIjpudWxsLCJwZXJzb24iOnsicGVyc29uX2lkIjoyLCJmaXJzdF9uYW1lIjoiSm9oYW4iLCJsYXN0X25hbWUiOiJTdGF0aW9uTWFuYWdlciIsInRpdGxlX2NvZGUiOiJNUiIsImRhdGVfb2ZfYmlydGgiOiIxOTc0LTAxLTAxIiwic2V4X2NvZGUiOiJNIiwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInNleFJlZiI6eyJzZXhfY29kZSI6Ik0iLCJuYW1lIjoiTWFsZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfSwicGVyc29uQWRkcmVzcyI6eyJwZXJzb25fYWRkcmVzc19pZCI6MiwiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwicGVyc29uX2lkIjoyLCJhZGRyZXNzX2lkIjoxLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbCwiYWRkcmVzc1R5cGVSZWYiOnsiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwibmFtZSI6Ik9mZmljZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfX19LCJ1c2VyUm9sZVJlZiI6eyJ1c2VyX3JvbGVfY29kZSI6IlNNIiwibmFtZSI6IlN0YXRpb24gTWFuYWdlciIsImNyZWF0aW9uX2RhdGVfdGltZSI6IjIwMjAtMDctMDFUMDA6MDA6MDAuMDAwWiIsInBlcm1pc3Npb24iOiI_Pz8_IiwidXNlcl9yb2xlX2dyb3VwX2NvZGUiOiJDUyIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJ1c2VyUm9sZUdyb3VwUmVmIjp7InVzZXJfcm9sZV9ncm91cF9jb2RlIjoiQ1MiLCJuYW1lIjoiQ2hhcmdpbmcgU2l0ZSIsImVuZHBvaW50IjoiYXV0aGVudGljYXRlX2V2X2NoYXJnaW5nX3NpdGVfc3RhZmYiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH19fSwidXNlY2FzZXNQZXJtaXR0ZWQiOlt7InVzZXJfcGVybWlzc2lvbl91c2VjYXNlX2lkIjoxLCJuYW1lIjoiU2V0dXAgUHJpY2VzIiwiZGVzY3JpcHRpb24iOiJNYWtlIGNoYW5nZXMgdG8gQ2hhcmdlIEl0ZXNtIiwidXJsX3BhdGgiOiIvY2hhcmdlX2l0ZW0iLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH0seyJ1c2VyX3Blcm1pc3Npb25fdXNlY2FzZV9pZCI6MiwibmFtZSI6IlNldHVwIFBsYW5zL0J1bmRsZXMiLCJkZXNjcmlwdGlvbiI6Ik1ha2UgY2hhbmdlcyB0byBQbGFucyBhbmQgQnVuZGxlcyIsInVybF9wYXRoIjoiL2J1bmRsZV9wcmljZXMiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH1dLCJldkNoYXJnaW5nU2l0ZXMiOlt7ImV2X2NoYXJnaW5nX3NpdGVfaWQiOjEyOSwibmFtZSI6InRlc3Qgc3RhdGlvbiIsImRlc2NyaXB0aW9uIjoiMzQgc2RzZHNkIiwiZ3BzX2xhdGl0dWRlIjoiMzEuNDkwMzA5NjIiLCJncHNfbG9uZ2l0dWRlIjoiNzQuNDAxNjQ1NjYiLCJkZWZhdWx0X2N1cnJlbmN5X2lkIjoxLCJ2YXRfaWQiOjEsImFkZHJlc3NfaWQiOjEzMCwiZXZfY2hhcmdpbmdfc2l0ZV9ncm91cF9pZCI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGx9XX1dLCJpYXQiOjE1OTc4MTE0MDl9.koNeFYpWWV07_QvQTd_QcfDam7q9LUpHi_dPP3BqWUY");

                call.enqueue(new Callback<UserListResponse>() {
                    @Override
                    public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {

                        if (response.isSuccessful() && response != null) {

                            UserListResponse webResponse = response.body();


                            if (webResponse.statusCode == 200) {
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponse(webResponse.getResponse().getOrders());
                                }
                            } else {
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponse(webResponse.message);
                                }


                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<UserListResponse> call, Throwable t) {
                        for (NetworkApiListener l : mListeners) {
                            l.onResponseError(t.getMessage());
                        }
                    }
                });

            }
        });

        return data;

    }

    public void sendPost(final String cashRecieved, final String id, final File file, final String deliveryBoyNotes, final String itemSelected) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse(file.toString());
                File file = new File(uri.getPath());
                RequestBody reqFile = new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {

                    }
                };

                Post post = new Post();
                post.setDeliveryBoyNotes(deliveryBoyNotes);
                if (itemSelected.equalsIgnoreCase("Delivered")) {
                    post.setOrderStatus("3");
                    reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                }

                post.setOrderId(id);
                post.setCashRecieved(cashRecieved);


                MultipartBody.Part upload = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
                RequestBody json = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (HelperMethod.convertJavaToJson(post)));
                Call<DeliveryStatusResponse> call = mNetworkClient.getNetworkWebService().dataSend(upload, json);

                call.enqueue(new Callback<DeliveryStatusResponse>() {
                    @Override
                    public void onResponse(Call<DeliveryStatusResponse> call, Response<DeliveryStatusResponse> response) {

                        DeliveryStatusResponse webResponse = response.body();
                        DeliveryStatus status = webResponse.getStatus();
                        if (response.isSuccessful() && response != null) {

                            for (NetworkApiListener l : mListeners) {
                                l.onResponse(status.getMessage(), status.getCode());
                                Log.i(TAG, "post submitted to API." + response.body().toString());
                            }

                        } else {
                            for (NetworkApiListener l : mListeners) {
                                l.onResponseError(status.getMessage());
                                Log.i(TAG, "post submitted to API." + webResponse.toString());
                            }

                        }
                    }


                    @Override
                    public void onFailure(Call<DeliveryStatusResponse> call, Throwable t) {
                        for (NetworkApiListener l : mListeners) {
                            l.onResponseError(t.getMessage());
                        }
                    }
                });

            }
        });
    }

    public void disputedCase(final String id, final File file, final String deliveryBoyNotes, final String itemSelected) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse(file.toString());
                File file = new File(uri.getPath());
                RequestBody reqFile = new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {

                    }
                };

                Post post = new Post();
                post.setDeliveryBoyNotes(deliveryBoyNotes);

                if (itemSelected.equalsIgnoreCase("Disputed")) {

                    post.setOrderStatus("5");
                    reqFile = RequestBody.create(MediaType.parse("audio/*"), file);

                }

                post.setOrderId(id);


                MultipartBody.Part upload = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
                RequestBody json = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (HelperMethod.convertJavaToJson(post)));
                Call<DeliveryStatusResponse> call = mNetworkClient.getNetworkWebService().dataSend(upload, json);

                call.enqueue(new Callback<DeliveryStatusResponse>() {
                    @Override
                    public void onResponse(Call<DeliveryStatusResponse> call, Response<DeliveryStatusResponse> response) {

                        DeliveryStatusResponse webResponse = response.body();
                        DeliveryStatus status = webResponse.getStatus();
                        if (status.getCode() == 200) {

                            for (NetworkApiListener l : mListeners) {
                                l.onResponse(status.getMessage(), status.getCode());
                                Log.i(TAG, "post submitted to API." + response.body().toString());
                            }

                        }
                        if (status.getCode() == 400) {
                            for (NetworkApiListener l : mListeners) {
                                l.onResponse(status.getMessage(), status.getCode());
                                Log.i(TAG, "post submitted to API." + webResponse.toString());
                            }

                        }
                    }


                    @Override
                    public void onFailure(Call<DeliveryStatusResponse> call, Throwable t) {
                        for (NetworkApiListener l : mListeners) {
                            l.onResponseError(t.getMessage());
                        }
                    }
                });

            }
        });
    }


    public void undelivered(final String id, final String deliveryBoyNotes, final String itemSelected) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                final Post post = new Post();

                if (itemSelected.equalsIgnoreCase("Undelivered"))
                    post.setOrderStatus("8");
                if (itemSelected.equalsIgnoreCase("Disputed"))
                    post.setOrderStatus("5");
                post.setOrderId(id);
                post.setDeliveryBoyNotes(deliveryBoyNotes);
                post.getDeliveryBoyNotes();

                Gson gson = new Gson();
                String Json = gson.toJson(post);

                RequestBody json = RequestBody.create(MediaType.parse("text/plain"), Json);
                Call<StatusResponse> call = mNetworkClient.getNetworkWebService().undeliveredService(json);

                call.enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                        StatusResponse webResponse = response.body();
                        Status status = webResponse.getStatus();
                        if (status.getCode() == 200) {

                            for (NetworkApiListener l : mListeners) {
                                l.onResponse(status.getMessage(), status.getCode());
                                Log.i(TAG, "post submitted to API." + response.body().toString());
                            }

                        }
                        if (status.getCode() == 400) {
                            for (NetworkApiListener l : mListeners) {
                                l.onResponse(status.getMessage(), status.getCode());
                                Log.i(TAG, "post submitted to API." + webResponse.toString());
                            }

                        }
                    }


                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        for (NetworkApiListener l : mListeners) {
                            l.onResponseError(t.getMessage());
                        }
                    }
                });

            }
        });
    }


    public synchronized void getItems(String orderId) {
        RequestOrderId Request = new RequestOrderId();
        Request.setOrder_id(orderId);

        Call<ProductsResponse> call = mNetworkClient.getNetworkWebService().getItems(Request);

        call.enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                try {
                    if (response.isSuccessful() && response != null) {
                        ProductsResponse itemslistobject = response.body();
                        ProductStatus itemsStatus = itemslistobject.getStatus();


                        if (itemsStatus.getCode() == 200 && itemsStatus != null) {
                            for (NetworkApiListener m : mListeners) {
                                m.onResponseItem(itemsStatus.getMessage());
                                m.onItemResponse(itemslistobject.getResponse().getProducts());
                            }
                        } else {
                            for (NetworkApiListener m : mListeners) {
                                m.onResponse(itemsStatus.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public synchronized void changeStatus(final String id, final String itemSelected) {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                RequestOrderId Request = new RequestOrderId();
                Request.setOrder_id(id);
                if (itemSelected.equalsIgnoreCase("Delivered")) {
                    Request.setorder_status("3");
                }

                if (itemSelected.equalsIgnoreCase("Disputed")) {

                    Request.setorder_status("5");

                }

                if (itemSelected.equalsIgnoreCase("Undelivered")) {

                    Request.setorder_status("8");

                }
                //Request.setorder_status("3");

                Call<Change_Order_Status> call = mNetworkClient.getNetworkWebService().updateStatus(Request);

                call.enqueue(new Callback<Change_Order_Status>() {
                    @Override
                    public void onResponse(Call<Change_Order_Status> call, Response<Change_Order_Status> response) {
                        try {
                            if (response.isSuccessful() && response != null) {
                                Change_Order_Status status_object = response.body();
                                com.example.khushbakht.grocerjin.model.OrderList.Status message_status = status_object.getStatus();
                                if (message_status.getCode() == 200 && status_object != null) {
                                    for (NetworkApiListener m : mListeners) {
                                        m.onResponseChangeStatus(message_status.getMessage());
                                    }
                                } else {
                                    for (NetworkApiListener m : mListeners) {
                                        m.onResponse(message_status.getMessage());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Change_Order_Status> call, Throwable t) {
                        t.printStackTrace();
                    }

                });

            }

        });
    }

    public synchronized ProfileResponse profileInfo() {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {


                final ProfileRequest profileRequest = new ProfileRequest();
                profileRequest.setE_id(loginData.getResponse().getEId());

                Call<ProfileResponse> call = mNetworkClient.getNetworkWebService().getProfile(profileRequest);

                call.enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                        if (response.isSuccessful() && response != null) {

                            ProfileResponse webResponse = response.body();

                            com.example.khushbakht.grocerjin.model.profile.Status status = webResponse.getStatus();


                            if (status.getCode() == 200) {
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponseProfile(status.getMessage(), status.getCode(), webResponse.getResponse().getName(), webResponse.getResponse().getUsername(), webResponse.getResponse().getLocation(), webResponse.getResponse().getLatitude(), webResponse.getResponse().getLongitude());

                                }
                            } else {
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponse(status.getMessage());
                                }


                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        for (NetworkApiListener l : mListeners) {
                            l.onResponseError(t.getMessage());
                        }
                    }
                });

            }
        });

        return profileResponse;

    }

    public synchronized void pickOrder(final String orderId) {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                PickModel request = new PickModel();
                sharedPreferences = mContext.getSharedPreferences(RememberCredentials.SharedPreferenceName, Context.MODE_PRIVATE);
                request.driverId = Integer.parseInt(sharedPreferences.getString(AppConstant.DRIVERID, "0"));
                request.StatusId = 4;

              /*  request.setOrder_id(id);
                request.setDeliveryman_id(loginData.getResponse().getEId());
                request.setOrder_status("4");
                request.setLocation_id(loginData.getResponse().getLocation_id());
                request.setLocation_name(loginData.getResponse().getLocation_name());*/


                //Request.setorder_status("3");

                Call<PickResponse> call = mNetworkClient.getNetworkWebService().pickUpOrder("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjkzNDc0MDksImRhdGEiOlt7ImV2X2NoYXJnaW5nX3NpdGVfc3RhZmZfaWQiOjEsImV2X2NoYXJnaW5nX3NpdGVfaWQiOjEsInVzZXJfYWNjb3VudF9pZCI6Miwic3RhcnRfZGF0ZV90aW1lIjoiMjAyMC0wNy0wMVQwMDowMDowMC4wMDBaIiwiZW5kX2RhdGVfdGltZSI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInVzZXJBY2NvdW50Ijp7InVzZXJfYWNjb3VudF9pZCI6MiwidXNlcl9uYW1lIjoiam9oYW5zdGF0aW9ubWdyIiwiZW1haWxfYWRkcmVzcyI6ImpvaGFuQGFiYy5jb20iLCJwZXJzb25faWQiOjIsInBhc3N3b3JkIjoiQCNAQEAjIiwidXNlcl9yb2xlX2NvZGUiOiJTTSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJtb2JpbGVfcGhvbmVfbnVtYmVyIjpudWxsLCJwZXJzb24iOnsicGVyc29uX2lkIjoyLCJmaXJzdF9uYW1lIjoiSm9oYW4iLCJsYXN0X25hbWUiOiJTdGF0aW9uTWFuYWdlciIsInRpdGxlX2NvZGUiOiJNUiIsImRhdGVfb2ZfYmlydGgiOiIxOTc0LTAxLTAxIiwic2V4X2NvZGUiOiJNIiwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInNleFJlZiI6eyJzZXhfY29kZSI6Ik0iLCJuYW1lIjoiTWFsZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfSwicGVyc29uQWRkcmVzcyI6eyJwZXJzb25fYWRkcmVzc19pZCI6MiwiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwicGVyc29uX2lkIjoyLCJhZGRyZXNzX2lkIjoxLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbCwiYWRkcmVzc1R5cGVSZWYiOnsiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwibmFtZSI6Ik9mZmljZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfX19LCJ1c2VyUm9sZVJlZiI6eyJ1c2VyX3JvbGVfY29kZSI6IlNNIiwibmFtZSI6IlN0YXRpb24gTWFuYWdlciIsImNyZWF0aW9uX2RhdGVfdGltZSI6IjIwMjAtMDctMDFUMDA6MDA6MDAuMDAwWiIsInBlcm1pc3Npb24iOiI_Pz8_IiwidXNlcl9yb2xlX2dyb3VwX2NvZGUiOiJDUyIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJ1c2VyUm9sZUdyb3VwUmVmIjp7InVzZXJfcm9sZV9ncm91cF9jb2RlIjoiQ1MiLCJuYW1lIjoiQ2hhcmdpbmcgU2l0ZSIsImVuZHBvaW50IjoiYXV0aGVudGljYXRlX2V2X2NoYXJnaW5nX3NpdGVfc3RhZmYiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH19fSwidXNlY2FzZXNQZXJtaXR0ZWQiOlt7InVzZXJfcGVybWlzc2lvbl91c2VjYXNlX2lkIjoxLCJuYW1lIjoiU2V0dXAgUHJpY2VzIiwiZGVzY3JpcHRpb24iOiJNYWtlIGNoYW5nZXMgdG8gQ2hhcmdlIEl0ZXNtIiwidXJsX3BhdGgiOiIvY2hhcmdlX2l0ZW0iLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH0seyJ1c2VyX3Blcm1pc3Npb25fdXNlY2FzZV9pZCI6MiwibmFtZSI6IlNldHVwIFBsYW5zL0J1bmRsZXMiLCJkZXNjcmlwdGlvbiI6Ik1ha2UgY2hhbmdlcyB0byBQbGFucyBhbmQgQnVuZGxlcyIsInVybF9wYXRoIjoiL2J1bmRsZV9wcmljZXMiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH1dLCJldkNoYXJnaW5nU2l0ZXMiOlt7ImV2X2NoYXJnaW5nX3NpdGVfaWQiOjEsIm5hbWUiOiJDaGFyZ2luZyBTaXRlIFBsYXphIiwiZGVzY3JpcHRpb24iOiJDaGFyZ2luZyBTaXRlIFBsYXphIGFuZCBTaG9wcGluZyBNYWxsIiwiZ3BzX2xhdGl0dWRlIjoiNTkuMzI2MTQyIiwiZ3BzX2xvbmdpdHVkZSI6IjE3Ljk4MjA1MyIsImRlZmF1bHRfY3VycmVuY3lfaWQiOjEsInZhdF9pZCI6MSwiYWRkcmVzc19pZCI6MiwiZXZfY2hhcmdpbmdfc2l0ZV9ncm91cF9pZCI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGx9XX0seyJldl9jaGFyZ2luZ19zaXRlX3N0YWZmX2lkIjoyLCJldl9jaGFyZ2luZ19zaXRlX2lkIjoxMjksInVzZXJfYWNjb3VudF9pZCI6Miwic3RhcnRfZGF0ZV90aW1lIjoiMjAyMC0wNy0wMVQwMDowMDowMC4wMDBaIiwiZW5kX2RhdGVfdGltZSI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInVzZXJBY2NvdW50Ijp7InVzZXJfYWNjb3VudF9pZCI6MiwidXNlcl9uYW1lIjoiam9oYW5zdGF0aW9ubWdyIiwiZW1haWxfYWRkcmVzcyI6ImpvaGFuQGFiYy5jb20iLCJwZXJzb25faWQiOjIsInBhc3N3b3JkIjoiQCNAQEAjIiwidXNlcl9yb2xlX2NvZGUiOiJTTSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJtb2JpbGVfcGhvbmVfbnVtYmVyIjpudWxsLCJwZXJzb24iOnsicGVyc29uX2lkIjoyLCJmaXJzdF9uYW1lIjoiSm9oYW4iLCJsYXN0X25hbWUiOiJTdGF0aW9uTWFuYWdlciIsInRpdGxlX2NvZGUiOiJNUiIsImRhdGVfb2ZfYmlydGgiOiIxOTc0LTAxLTAxIiwic2V4X2NvZGUiOiJNIiwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGwsInNleFJlZiI6eyJzZXhfY29kZSI6Ik0iLCJuYW1lIjoiTWFsZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfSwicGVyc29uQWRkcmVzcyI6eyJwZXJzb25fYWRkcmVzc19pZCI6MiwiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwicGVyc29uX2lkIjoyLCJhZGRyZXNzX2lkIjoxLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbCwiYWRkcmVzc1R5cGVSZWYiOnsiYWRkcmVzc190eXBlX2NvZGUiOiJPIiwibmFtZSI6Ik9mZmljZSIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsfX19LCJ1c2VyUm9sZVJlZiI6eyJ1c2VyX3JvbGVfY29kZSI6IlNNIiwibmFtZSI6IlN0YXRpb24gTWFuYWdlciIsImNyZWF0aW9uX2RhdGVfdGltZSI6IjIwMjAtMDctMDFUMDA6MDA6MDAuMDAwWiIsInBlcm1pc3Npb24iOiI_Pz8_IiwidXNlcl9yb2xlX2dyb3VwX2NvZGUiOiJDUyIsImxhc3RfdXBkYXRlZF9ieSI6bnVsbCwibGFzdF91cGRhdGVkX2F0IjpudWxsLCJ1c2VyUm9sZUdyb3VwUmVmIjp7InVzZXJfcm9sZV9ncm91cF9jb2RlIjoiQ1MiLCJuYW1lIjoiQ2hhcmdpbmcgU2l0ZSIsImVuZHBvaW50IjoiYXV0aGVudGljYXRlX2V2X2NoYXJnaW5nX3NpdGVfc3RhZmYiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH19fSwidXNlY2FzZXNQZXJtaXR0ZWQiOlt7InVzZXJfcGVybWlzc2lvbl91c2VjYXNlX2lkIjoxLCJuYW1lIjoiU2V0dXAgUHJpY2VzIiwiZGVzY3JpcHRpb24iOiJNYWtlIGNoYW5nZXMgdG8gQ2hhcmdlIEl0ZXNtIiwidXJsX3BhdGgiOiIvY2hhcmdlX2l0ZW0iLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH0seyJ1c2VyX3Blcm1pc3Npb25fdXNlY2FzZV9pZCI6MiwibmFtZSI6IlNldHVwIFBsYW5zL0J1bmRsZXMiLCJkZXNjcmlwdGlvbiI6Ik1ha2UgY2hhbmdlcyB0byBQbGFucyBhbmQgQnVuZGxlcyIsInVybF9wYXRoIjoiL2J1bmRsZV9wcmljZXMiLCJsYXN0X3VwZGF0ZWRfYnkiOm51bGwsImxhc3RfdXBkYXRlZF9hdCI6bnVsbH1dLCJldkNoYXJnaW5nU2l0ZXMiOlt7ImV2X2NoYXJnaW5nX3NpdGVfaWQiOjEyOSwibmFtZSI6InRlc3Qgc3RhdGlvbiIsImRlc2NyaXB0aW9uIjoiMzQgc2RzZHNkIiwiZ3BzX2xhdGl0dWRlIjoiMzEuNDkwMzA5NjIiLCJncHNfbG9uZ2l0dWRlIjoiNzQuNDAxNjQ1NjYiLCJkZWZhdWx0X2N1cnJlbmN5X2lkIjoxLCJ2YXRfaWQiOjEsImFkZHJlc3NfaWQiOjEzMCwiZXZfY2hhcmdpbmdfc2l0ZV9ncm91cF9pZCI6bnVsbCwibGFzdF91cGRhdGVkX2J5IjpudWxsLCJsYXN0X3VwZGF0ZWRfYXQiOm51bGx9XX1dLCJpYXQiOjE1OTc4MTE0MDl9.koNeFYpWWV07_QvQTd_QcfDam7q9LUpHi_dPP3BqWUY",
                        orderId, request);

                call.enqueue(new Callback<PickResponse>() {
                    @Override
                    public void onResponse(Call<PickResponse> call, Response<PickResponse> response) {
                        try {
                            if (response.isSuccessful() && response != null) {
                                PickResponse status_object = response.body();
                                if (status_object.statusCode == 200 && status_object != null) {
                                    for (NetworkApiListener m : mListeners) {
                                        m.onPickResponse(status_object.message, status_object.statusCode);
                                    }
                                } else {
                                    for (NetworkApiListener m : mListeners) {
                                        m.onPickResponse(status_object.message, status_object.statusCode);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<PickResponse> call, Throwable t) {
                        t.printStackTrace();
                    }

                });

            }

        });
    }


//    public synchronized void deviceIdentity(final String deviceType, final String deviceId) {
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                Call<List<String>> call = mNetworkClient.getNetworkWebService().deviceIdentity(deviceType, deviceId);
//                call.enqueue(new Callback<List<String>>() {
//                    @Override
//                    public void onResponse(Call<List<String>> call, OrderList<List<String>> response) {
//
//                        if (response.isSuccessful()) {
//                            String identity = response.raw().header("Identity");
//                            ParkingProPref.getInstance(mContext).setUserIdentity(identity);
//                        }
//
//                        for (NetworkApiListener l : mListeners) {
//                            l.onDeviceTokenResponse("");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<String>> call, Throwable throwable) {
//                        if (throwable instanceof IOException) {
//                            Validation.showToastMsg(mContext, "" + throwable.getMessage());
//                        } else if (throwable instanceof SocketTimeoutException) {
//                            Validation.showToastMsg(mContext, "" + throwable.getMessage());
//                        } else {
//                            Validation.showToastMsg(mContext, "" + throwable.getMessage());
//                        }
//
//                        for (NetworkApiListener l : mListeners) {
//                            l.onDeviceTokenFail("");
//                        }
//                    }
//                });
//
//
//            }
//        });
//    }
//
//    public synchronized void putUserModel(final String identity, final String userName, final String email, final String facebook_id, final String google_id, final String mobile,
//                                          final String loginType, final String socialAvatar) {
//
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                Call<UserResponse> userResponseCall = mNetworkClient.getNetworkWebService().userInfo(identity, userName, email, facebook_id, google_id, mobile, loginType, socialAvatar);
//
//                userResponseCall.enqueue(new Callback<UserResponse>() {
//                    @Override
//                    public void onResponse(Call<UserResponse> call, OrderList<UserResponse> response) {
//                        if (response.isSuccessful()) {
//                            UserResponse userResponse = response.body();
//                            UserModel userModel = userResponse.getData();
//                            SecurityModel securityModel = new SecurityModel();
//                            securityModel.setAuthToken(getAuthToken(response));
//                            securityModel.setPhpAuthToken(userModel.getFacebookId().concat(":").concat(userModel.getGoogleId()));
//                            ParkingProPref.getInstance(mContext).setUserAuthToken(getAuthToken(response));
//
//                            if (userModel != null) {
//                                ParkingProPref.getInstance(mContext).setUserModel(userModel);
//                                if (userModel.getAllowNotification() == 1)
//                                    ParkingProPref.getInstance(mContext).setPrefNotificationStatus(true);
//                            }
//
//                            for (NetworkApiListener l : mListeners) {
//                                l.onUserResponse(userResponse);
//                            }
//                        } else {
//                            for (NetworkApiListener l : mListeners) {
//                                l.onUserResponseFail(response.message());
//                            }
//
//                        }
//                    }
//
//
//                    @Override
//                    public void onFailure(Call<UserResponse> call, Throwable t) {
//                        for (NetworkApiListener l : mListeners) {
//                            if (t instanceof IOException) {
//                                l.onUserResponseFail("" + t.getMessage());
//                            } else if (t instanceof SocketTimeoutException) {
//                                l.onUserResponseFail("" + t.getMessage());
//                            } else {
//                                l.onUserResponseFail("" + t.getMessage());
//                            }
//                        }
//                    }
//                });
//
//
//            }
//        });
//
//    }


    private String getAuthToken(Response response) {
        String authToken = "";
        okhttp3.Headers allHeaders = response.headers();
        authToken = allHeaders.get("Token");
        return authToken;
    }
}
