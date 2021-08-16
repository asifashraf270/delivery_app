package com.example.khushbakht.grocerjin.network;

import com.example.khushbakht.grocerjin.model.OrderList.OrderList;
import com.example.khushbakht.grocerjin.model.Postdelivery.Post;
import com.example.khushbakht.grocerjin.model.productList.Product;
import com.example.khushbakht.grocerjin.responseentity.OrderDetailResponse;

import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Khushbakht on 8/10/2017.
 */

public abstract class NetworkApiListener {

    public void onResponse(final String statusMessage){}
    public void onPickResponse(final String statusMessage, int code){}
    public void onResponseLogin(final String statusMessage, final int statusCode){}
    public void onResponseProfile(final String statusMessage, int code, String name, String username, String location, String lat, String lng){}
    public void onResponseItem(final String statusMessage){}
    public void onResponseChangeStatus(final String statusMessage){}
    public void onResponseError(final String statusMessageError){}
    public void onResponse(List<OrderList> userListResponses) {}
    public void onItemResponse(OrderDetailResponse ProductListResponses){}

    public void onResponse(final String statusMessage,final int code){}
}
