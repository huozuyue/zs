package com.hzy.service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hzy on 2018/7/5.
 */

public interface OrderService {
    @POST("handleOrder")
    Call<ResponseBody> submitOrder(@Header("token") String token, @Query("jsonstring") String
            token1);

    //@FormUrlEncoded
    @POST("handleOrder")
    Call<ResponseBody> submitOrderInfo(@Body RequestBody route);

    //@FormUrlEncoded
    @POST("handleOrder")
    Call<ResponseBody> submitOrderInfo1(@Header("token") String token, @Query("jsonstring")
            String jsonstring);

    @POST("getOrder")
    Call<ResponseBody> getOrderInfo();

    @POST("getOrderDetail")
    Call<ResponseBody> getOrderDetail(@Body RequestBody route);

}
