package com.hzy.util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

/**
 * Created by hzy on 2018/2/6.
 */

public class RetrofitUtil {

    public static final String SERVER_URL = "http://172.25.220.8:8080/arest/services/";
    public static Retrofit provideClientApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(genericClient())
                .build();
        return retrofit;
    }


    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                // .addHeader("token", "jia de token")
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "application/json")
                                .addHeader("Cookie", "add cookies here")
                                .build();

                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }
}
