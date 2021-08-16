package com.example.khushbakht.grocerjin.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Process;
import android.util.Log;

import com.example.khushbakht.grocerjin.AppConstant;
import com.example.khushbakht.grocerjin.Controller;
import com.example.khushbakht.grocerjin.RememberCredentials;
import com.example.khushbakht.grocerjin.activities.Login;
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
import com.example.khushbakht.grocerjin.model.productList.GeneralResponseModel;
import com.example.khushbakht.grocerjin.model.productList.ProductList;
import com.example.khushbakht.grocerjin.model.productList.ProductStatus;
import com.example.khushbakht.grocerjin.model.productList.ProductsResponse;
import com.example.khushbakht.grocerjin.model.productList.RequestOrderId;
import com.example.khushbakht.grocerjin.model.profile.ProfileRequest;
import com.example.khushbakht.grocerjin.model.profile.ProfileResponse;
import com.example.khushbakht.grocerjin.network.NetworkApiListener;
import com.example.khushbakht.grocerjin.model.login.response.StatusResponse;
import com.example.khushbakht.grocerjin.network.NetworkClient;
import com.example.khushbakht.grocerjin.responseentity.OrderDetailResponseModel;
import com.example.khushbakht.grocerjin.responseentity.login.LoginResponse;
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
                Call<LoginResponse> call = mNetworkClient.getNetworkWebService().driverLogin(postSignUp);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response != null) {
                            /* loginData = response.body();*/
                            /*  Status status = loginData.getStatus();
                             */

                            if (response.body().getStatusCode() == 200) {
                                sharedPreferences.edit().putString(AppConstant.DRIVERID, response.body().getResponse().getDriverDetail().getDriverId() + "").commit();
                                sharedPreferences.edit().putString(AppConstant.TOKEN, response.body().getResponse().getToken()).commit();
                              /*  if (loginData.getResponse().getLocation_name() != null) {
                                    sharedPreferences.edit().putString(AppConstant.DRIVERID, loginData.getResponse().getEId()).commit();
//                                    FirebaseMessaging.getInstance().subscribeToTopic(loginData.getResponse().getPush_name());
                                }*/
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponseLogin(response.body().getMessage(), response.body().getStatusCode());
                                }
                            } else {
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponseLogin(response.body().getMessage(), response.body().getStatusCode());
                                }
                            }
                        } else {
                            for (NetworkApiListener l : mListeners) {
                                l.onResponseError(response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
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
                Call<ResponseBody> responseBodyCall = mNetworkClient.getNetworkWebService().saveToken(model, sharedPreferences.getString(AppConstant.TOKEN, ""));
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
                sharedPreferences = mContext.getSharedPreferences(RememberCredentials.SharedPreferenceName, Context.MODE_PRIVATE);


                Call<UserListResponse> call = mNetworkClient.getNetworkWebService().orderList(sharedPreferences.getString(AppConstant.TOKEN, ""));

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
                int statusId = 0;
                if (itemSelected.equalsIgnoreCase("Delivered")) {
                    statusId = 3;
                } else {
                    statusId = 5;
                }
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
/*
                Post post = new Post();
                post.setDeliveryBoyNotes(deliveryBoyNotes);
                if (itemSelected.equalsIgnoreCase("Delivered")) {
                    post.setOrderStatus("3");
                    reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                }

                post.setOrderId(id);
                post.setCashRecieved(cashRecieved);*/
                sharedPreferences = mContext.getSharedPreferences(RememberCredentials.SharedPreferenceName, Context.MODE_PRIVATE);

                MultipartBody.Part upload = MultipartBody.Part.createFormData("file_name", file.getName(), reqFile);
//                RequestBody json = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (HelperMethod.convertJavaToJson(post)));
                Call<GeneralResponseModel> call = mNetworkClient.getNetworkWebService().completeOrder(upload, id, statusId, deliveryBoyNotes, sharedPreferences.getString(AppConstant.TOKEN, ""));

                call.enqueue(new Callback<GeneralResponseModel>() {
                    @Override
                    public void onResponse(Call<GeneralResponseModel> call, Response<GeneralResponseModel> response) {

                      /*  DeliveryStatusResponse webResponse = response.body();
                        DeliveryStatus status = webResponse.getStatus();*/
                        if (response.isSuccessful() && response != null) {

                            for (NetworkApiListener l : mListeners) {
                                l.onResponse(response.body().message, response.body().statusCode);
                                Log.i(TAG, "post submitted to API." + response.body().toString());
                            }

                        } else {
                            for (NetworkApiListener l : mListeners) {
                                l.onResponseError(response.message());
                            }

                        }
                    }


                    @Override
                    public void onFailure(Call<GeneralResponseModel> call, Throwable t) {
                        for (NetworkApiListener l : mListeners) {
                            l.onResponseError(t.getMessage());
                        }
                    }
                });

            }
        });
    }

   /* public void disputedCase(final String id, final File file, final String deliveryBoyNotes, final String itemSelected) {
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
*/

    public void undelivered(final String id, final String deliveryBoyNotes, final String itemSelected) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                final Post post = new Post();
                post.statusId = 8;
                post.reason = deliveryBoyNotes;
                sharedPreferences = mContext.getSharedPreferences(RememberCredentials.SharedPreferenceName, Context.MODE_PRIVATE);

              /*  if (itemSelected.equalsIgnoreCase("Undelivered"))
                    post.setOrderStatus("8");
                if (itemSelected.equalsIgnoreCase("Disputed"))
                    post.setOrderStatus("5");
                post.setOrderId(id);
                post.setDeliveryBoyNotes(deliveryBoyNotes);
                post.getDeliveryBoyNotes();*/

                Gson gson = new Gson();
                String Json = gson.toJson(post);

                RequestBody json = RequestBody.create(MediaType.parse("text/plain"), Json);
                Call<GeneralResponseModel> call = mNetworkClient.getNetworkWebService().undeliveredOrder(Integer.parseInt(id), post, sharedPreferences.getString(AppConstant.TOKEN, ""));

                call.enqueue(new Callback<GeneralResponseModel>() {
                    @Override
                    public void onResponse(Call<GeneralResponseModel> call, Response<GeneralResponseModel> response) {


                        if (response.body().statusCode == 200) {

                            for (NetworkApiListener l : mListeners) {
                                l.onResponse(response.body().message, response.body().statusCode);
                                Log.i(TAG, "post submitted to API." + response.body().toString());
                            }

                        } else {
                            for (NetworkApiListener l : mListeners) {
                                l.onResponseError(response.message());
                            }
                        }
                        /*if (status.getCode() == 400) {
                            for (NetworkApiListener l : mListeners) {
                                l.onResponse(status.getMessage(), status.getCode());
                                Log.i(TAG, "post submitted to API." + webResponse.toString());
                            }

                        }*/
                    }


                    @Override
                    public void onFailure(Call<GeneralResponseModel> call, Throwable t) {
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
        sharedPreferences = mContext.getSharedPreferences(RememberCredentials.SharedPreferenceName, Context.MODE_PRIVATE);

        Call<OrderDetailResponseModel> call = mNetworkClient.getNetworkWebService().getOrderDetail(Integer.parseInt(orderId), sharedPreferences.getString(AppConstant.TOKEN, ""));
        call.enqueue(new Callback<OrderDetailResponseModel>() {
            @Override
            public void onResponse(Call<OrderDetailResponseModel> call, Response<OrderDetailResponseModel> response) {
                try {
                    if (response.isSuccessful() && response != null) {
                        OrderDetailResponseModel itemslistobject = response.body();


                        if (itemslistobject.statusCode == 200 && itemslistobject != null) {
                            for (NetworkApiListener m : mListeners) {
                                m.onResponseItem(itemslistobject.message);
                                m.onItemResponse(itemslistobject.response);
                            }
                        } else {
                            for (NetworkApiListener m : mListeners) {
                                m.onResponse(itemslistobject.message);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<OrderDetailResponseModel> call, Throwable t) {
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


                /*final ProfileRequest profileRequest = new ProfileRequest();
                profileRequest.setE_id(loginData.getResponse().getEId());*/
                sharedPreferences = mContext.getSharedPreferences(RememberCredentials.SharedPreferenceName, Context.MODE_PRIVATE);


                Call<LoginResponse> call = mNetworkClient.getNetworkWebService().getDriverProfile(sharedPreferences.getString(AppConstant.DRIVERID,"0"),sharedPreferences.getString(AppConstant.TOKEN,""));

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.isSuccessful() && response != null) {


                            if (response.body().getStatusCode() == 200) {
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponseProfile(response.body().getMessage(), response.body().getStatusCode(), response.body().getResponse().getDriverDetail().getDriverName(), response.body().getResponse().getDriverDetail().getDriverEmail(), "", "", "");

                                }
                            } else {
                                for (NetworkApiListener l : mListeners) {
                                    l.onResponse(response.message());
                                }


                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
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

                Call<PickResponse> call = mNetworkClient.getNetworkWebService().pickUpOrder(sharedPreferences.getString(AppConstant.TOKEN, ""),
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
