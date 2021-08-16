package com.example.khushbakht.grocerjin.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Khushbakht on 8/10/2017.
 */

public class NetworkClient {

    private static final String TAG = NetworkClient.class.getCanonicalName();

    private static NetworkWebService networkWebService;
    private static NetworkClient networkClient;
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://meatonwheel.com/api/";



    /**
     * Live Url
     */


    public static NetworkClient getClient() {
        try {
            if (networkWebService == null) {

                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic("android", "android");
                        //   LogUtil.info(TAG , credential);
                        return response.request().newBuilder().header("Authorization", credential).build();
                    }
                }).addInterceptor(interceptor)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .build();

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();


                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                networkWebService = retrofit.create(NetworkWebService.class);
                networkClient = new NetworkClient();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return networkClient;
    }

    public static NetworkWebService getNetworkWebService() {
        return networkWebService;
    }

    public static void setBuffaloWebService(NetworkWebService networkWebService) {
        NetworkClient.networkWebService = networkWebService;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        NetworkClient.retrofit = retrofit;
    }


}
