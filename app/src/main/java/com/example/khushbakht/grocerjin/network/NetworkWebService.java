package com.example.khushbakht.grocerjin.network;

import com.example.khushbakht.grocerjin.AppConstant;
import com.example.khushbakht.grocerjin.firebase.SaveFirebaseTokenModel;
import com.example.khushbakht.grocerjin.model.OrderList.UserListRequest;
import com.example.khushbakht.grocerjin.model.OrderList.UserListResponse;
import com.example.khushbakht.grocerjin.model.Postdelivery.DeliveryStatusResponse;
import com.example.khushbakht.grocerjin.model.Postdelivery.Post;
import com.example.khushbakht.grocerjin.model.StatusUpdate.Change_Order_Status;
import com.example.khushbakht.grocerjin.model.login.PostSignUp;
import com.example.khushbakht.grocerjin.model.login.response.LoginData;
import com.example.khushbakht.grocerjin.model.pickOrder.PickModel;
import com.example.khushbakht.grocerjin.model.pickOrder.PickResponse;
import com.example.khushbakht.grocerjin.model.productList.GeneralResponseModel;
import com.example.khushbakht.grocerjin.model.productList.ProductsResponse;
import com.example.khushbakht.grocerjin.model.productList.RequestOrderId;
import com.example.khushbakht.grocerjin.model.login.response.StatusResponse;
import com.example.khushbakht.grocerjin.model.profile.ProfileRequest;
import com.example.khushbakht.grocerjin.model.profile.ProfileResponse;
import com.example.khushbakht.grocerjin.responseentity.OrderDetailResponseModel;
import com.example.khushbakht.grocerjin.responseentity.login.LoginResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Khushbakht on 8/10/2017.
 */

public interface NetworkWebService {

    @POST("paker_login.php")
    Call<LoginData> login(@Body RequestBody body);

    @POST("delivery_man.php")
    Call<UserListResponse> getUserList(@Body UserListRequest body);

    @POST("order_products.php")
    Call<ProductsResponse> getItems(@Body RequestOrderId body);

    @GET(NetworkClient.FIRE_BASE_TOKEN_BASE + AppConstant.ORDER_DETAIL + "/{orderId}")
    Call<OrderDetailResponseModel> getOrderDetail(@Path("orderId") int orderId, @Header("token") String token);


    @Multipart
    @POST("deliveryman_orderstatus.php")
    Call<DeliveryStatusResponse> dataSend(@Part MultipartBody.Part file, @Part("json") RequestBody json);

    @Multipart
    @PUT(NetworkClient.FIRE_BASE_TOKEN_BASE + AppConstant.COMPLETE_ORDER + "/{orderId}")
    Call<GeneralResponseModel> completeOrder(@Part MultipartBody.Part file, @Path("orderId") String orderId, @Part("status_id") int statusId, @Part("reason") String reason, @Header("token") String token);

    @Multipart
    @POST("deliveryman_orderstatus_undelivered.php")
    Call<StatusResponse> undeliveredService(@Part("json") RequestBody json);

    @PUT(NetworkClient.FIRE_BASE_TOKEN_BASE + AppConstant.UPDATE_STATUS + "/{orderId}")
    Call<GeneralResponseModel> undeliveredOrder(@Path("orderId") int orderId, @Body Post model, @Header("token") String token);

    @POST("order_status_update.php")
    Call<Change_Order_Status> updateStatus(@Body RequestOrderId body);

    @POST("getProfile.php")
    Call<ProfileResponse> getProfile(@Body ProfileRequest body);

    @GET(NetworkClient.FIRE_BASE_TOKEN_BASE + AppConstant.GET_DRIVER_PROFILE + "/{driverId}")
    Call<LoginResponse> getDriverProfile(@Path("driverId") String driverId, @Header("token") String token);

    @POST("order_assigned_forDelivery.php")
    Call<PickResponse> pickService(@Body PickModel body);

    @POST(NetworkClient.FIRE_BASE_TOKEN_BASE + AppConstant.SAVEFIREBASETOKEN)
    Call<ResponseBody> saveToken(@Body SaveFirebaseTokenModel model, @Header("token") String token);

    @GET(NetworkClient.FIRE_BASE_TOKEN_BASE + AppConstant.ORDER_LIST)
    Call<UserListResponse> orderList(@Header("token") String token);

    @PUT(NetworkClient.FIRE_BASE_TOKEN_BASE + AppConstant.PICK_UP_ORDER + "/{order_id}")
    Call<PickResponse> pickUpOrder(@Header("token") String token, @Path("order_id") String orderId, @Body PickModel pickModel);

    @POST(NetworkClient.FIRE_BASE_TOKEN_BASE + AppConstant.LOGIN)
    Call<LoginResponse> driverLogin(@Body PostSignUp signUp);

}
