package com.hzy.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hzy.lovenum.LoginActivity;
import com.example.hzy.lovenum.R;
import com.example.hzy.lovenum.mainActivity;
import com.example.hzy.lovenum.myOrde;
import com.hzy.model.UserOrder;
import com.hzy.service.OrderService;
import com.hzy.serviceimpl.OrderServiceImpl;
import com.hzy.serviceimpl.SharedPreferencesHelper;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hzy on 2018/2/6.
 */

public class RetrofitUtil {
    static List<UserOrder> retList = new ArrayList<>();
    private static String token = "";

    //public static final String SERVER_URL = "http://169.254.154.25:8080/arest/services/test/";
    //public static final String SERVER_URL = "http://192.168.43.121:8080/arest/services/test/";
    //public static final String SERVER_URL = "http://172.:8080/arest/services/test/";
    //public static final String SERVER_URL = "http://192.168.1.4:8080/arest/services/test/";
    public static Retrofit provideClientApi(String SERVER_URL) {
        System.out.println("provideClientApi：*****");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(genericClient())
                .build();

        return retrofit;
    }

    public static Retrofit provideClientApi() {
        System.out.println("provideClientApi：*****");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.121:8080/arest/services/test/")
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
                                .addHeader("token", token)
                                .addHeader("Content-Type", "application/json;charset=UTF-8")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "application/json")
                                .addHeader("Cookie", "add cookies here")
                                .build();
                        //System.out.println("chain.proceed(request);：*****");

                        return chain.proceed(request);
                    }

                })
                .build();
        //System.out.println("httpClient：*****");

        return httpClient;
    }

    public static String getOrder(Context context) {
        String[] strs = new String[]{"strContentString", "detail_sendstr", "name_sendstr",
                "tel_sendstr", "", "", "", "", "", "", "", ""};

        String strContentString = "";
        String detail_sendstr = "";
        String name_sendstr = "";
        String tel_sendstr = "";
        strContentString = SharedPreferencesHelper.getInstance(context).getStringValue
                ("sendlocation");
        if ("".equals(strContentString)) {
            return "0";
        } else {
            detail_sendstr = SharedPreferencesHelper.getInstance(context).getStringValue
                    ("detail_sendstr");
        }
        if ("".equals(detail_sendstr)) {
            return "0";
        } else {
            name_sendstr = SharedPreferencesHelper.getInstance(context).getStringValue
                    ("name_sendstr");
        }
        if ("".equals(name_sendstr)) {
            return "0";
        } else {
            tel_sendstr = SharedPreferencesHelper.getInstance(context).getStringValue
                    ("tel_sendstr");
        }
        if ("".equals(tel_sendstr)) {
            return "0";
        } else {
            JSONObject Order = new JSONObject();

            try {
                Order.put("success", false);
                Order.put("msg", "case INVALID:您的token不合法或者过期了，请重新登陆");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return "0";
    }

    public static String getOrderStr(Context context) {
        JSONObject Order = new JSONObject();
        String[] strs = new String[]{"sendlocation", "detail_sendstr", "name_sendstr",
                "tel_sendstr", "receive_location",
                "receive_addressstr", "receive_namestr", "receive_phonestr"};
        String a = "";
        for (String s : strs) {
            a = SharedPreferencesHelper.getInstance(context).getStringValue
                    (s);
            Logger.d(s + "的值为：" + a);
            //System.out.println(s+"的值为："+a);
            if ("".equals(a)) {
                return s;
            } else {
                try {
                    Order.put(s, a);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] strsdoubble = new String[]{"sendlat", "sendlon", "receivelat", "receivelon"};
        double b = 0;
        for (String s : strsdoubble) {
            b = SharedPreferencesHelper.getInstance(context).getDoubleValue(s);
            System.out.println(s + "的值为：" + b + "。");
            if (0 == b) {
                return s;
            } else {
                try {
                    Order.put(s, b);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] strsint = new String[]{"dis", "dur", "taxiCost"};
        int c = 0;
        for (String s : strsint) {
            c = SharedPreferencesHelper.getInstance(context).getIntValue(s);
            //System.out.println(s+"的值为："+c+"。");
            if (0 == c) {
                return s;
            } else {
                try {
                    Order.put(s, c);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] strsnull = new String[]{"remarks_sendstr"};
        String d = "";
        for (String s : strsnull) {
            d = SharedPreferencesHelper.getInstance(context).getStringValue
                    (s);

            try {
                Order.put(s, d);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return Order.toString();
    }

    public static String beginRequest(final Context context, final mainActivity a) {
        a.showProgressDialog(1);
        String orderstr = getOrderStr(context);
        Logger.d("orderstr的值为：" + orderstr);
        if (orderstr.length() > 30) {

            token = SharedPreferencesHelper.getInstance(context).getStringValue("token");
            Logger.d("token取出，将要放到包头：" + token);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;" +
                    "charset=UTF-8"), orderstr);
            Properties properties = AppProperties.getProperties(context);
            //url = properties.getProperty("serverUrl");
            OrderService service = provideClientApi(properties.getProperty("serverUrl")).create
                    (OrderService.class);
            Call<ResponseBody> call = service.submitOrderInfo(body);
            //Call<ResponseBody> call = service.submitOrderInfo("aaaaabbbbbbbccccccccc");
            System.out.println("**call.toString();：" + call.toString());

            final String[] updateToken = {""};
            //开始异步请求

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println("请求成功操作：*****");
                    System.out.println("**url：" + call.request().url().toString());
                    updateToken[0] = response.headers().get("token");
                    System.out.println("**updateToken：" + updateToken[0]);
                    SharedPreferencesHelper.getInstance(context).putStringValue("token",
                            updateToken[0]);
                    token = SharedPreferencesHelper.getInstance(context).getStringValue("token");
                    Logger.d("更新后的token：" + token);

                    String jsonString = "";
                    //this.wait(2000);
                    try {
                        // Simulate network access.
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Toast.makeText(a, "模拟失败", Toast.LENGTH_LONG).show();
                    }
                    try {
                        //this.wait(5000);
                        //Thread.sleep(5000);
                        jsonString = response.body().string();
                        if (null == jsonString || "".equals(jsonString)) {
                            a.dissmissProgressDialog();
                            Toast.makeText(a, "网络异常", Toast.LENGTH_LONG).show();
                        } else {
                            System.out.println("**data：" + jsonString);
                            JSONObject dataJson = new JSONObject(jsonString);
                            String msg = dataJson.getString("msg");
                            System.out.println("msg：" + msg);
                            boolean successStatus = dataJson.getBoolean("success");

                            a.dissmissProgressDialog();
                            if (successStatus == true && msg.equals("ok")) {
                                System.out.println("msg：2" + msg);
                                Toast.makeText(a, "订单提交成功", Toast.LENGTH_LONG).show();

                            } else {
                                System.out.println("msg：1" + msg);
                                Intent intent = new Intent();
                                intent.setClass(context, LoginActivity.class);
                                a.startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(a, "程序异常", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> arg0, Throwable t) {
                    Toast.makeText(a, "网络异常", Toast.LENGTH_LONG).show();
                    //请求失败操作
                    System.out.println("请求失败操作1：*****" + t.getMessage());
                    System.out.println("请求失败操作2：*****" + t.fillInStackTrace().getCause());
                    System.out.println("请求失败操作3：*****" + t.fillInStackTrace().getLocalizedMessage
                            ());
                    //System.out.println("请求失败操作4：*****"+t.getCause().toString());
                    System.out.println("请求失败操作5：*****" + t.getStackTrace().toString());
                    System.out.println("请求失败操作6：*****" + arg0.request().url().toString());
                    // t.getMessage();


                    t.fillInStackTrace().printStackTrace();
                }
            });


        } else if (orderstr.equals("sendlocation")) {
            Toast.makeText(a, "请完善发件人定位信息", Toast.LENGTH_LONG).show();
        } else if (orderstr.equals("detail_sendstr")) {
            Toast.makeText(a, "请完善发件人详细地址", Toast.LENGTH_LONG).show();
        } else if (orderstr.equals("name_sendstr")) {
            Toast.makeText(a, "请完善发件人姓名", Toast.LENGTH_LONG).show();
        } else if (orderstr.equals("tel_sendstr")) {
            Toast.makeText(a, "请完善发件人电话", Toast.LENGTH_LONG).show();
        } else if (orderstr.equals("receive_addressstr")) {
            Toast.makeText(a, "请完善收件人详细地址", Toast.LENGTH_LONG).show();
        } else if (orderstr.equals("receive_namestr")) {
            Toast.makeText(a, "请完善收件人姓名", Toast.LENGTH_LONG).show();
        } else if (orderstr.equals("receive_phonestr")) {
            Toast.makeText(a, "请完善收件人电话", Toast.LENGTH_LONG).show();
        } else if (orderstr.equals("sendlat") || orderstr.equals("sendlon")) {
            Toast.makeText(a, "请完善发件人定位信息", Toast.LENGTH_LONG).show();
        } else if (orderstr.equals("receivelat") || orderstr.equals("receivelon")) {
            Toast.makeText(a, "请完善收件人定位信息", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(a, "程序异常", Toast.LENGTH_LONG).show();
        }
        a.dissmissProgressDialog();
        return "1";
    }

    @android.support.annotation.IdRes
    public static int getids(int i) {
        return i;
    }

    public static String getOrderInfo(final Context context, final myOrde a, final LinearLayout
            one) {

        token = SharedPreferencesHelper.getInstance(context).getStringValue("token");
        Properties properties = AppProperties.getProperties(context);
        //url = properties.getProperty("serverUrl");
        OrderService service = provideClientApi(properties.getProperty("serverUrl")).create
                (OrderService.class);
        Call<ResponseBody> call = service.getOrderInfo();


        final String[] updateToken = {""};

        //开始异步请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                updateToken[0] = response.headers().get("token");

                SharedPreferencesHelper.getInstance(context).putStringValue("token", updateToken
                        [0]);


                String jsonString = "";


                try {

                    jsonString = response.body().string();
                    if (null == jsonString || "".equals(jsonString)) {

                        Toast.makeText(a, "网络异常", Toast.LENGTH_LONG).show();
                    } else {
                        System.out.println("**data：" + jsonString);
                        JSONObject dataJson = new JSONObject(jsonString);
                        String msg = dataJson.getString("msg");
                        System.out.println("msg：" + msg);
                        boolean successStatus = dataJson.getBoolean("success");


                        if (successStatus == true && msg.equals("ok")) {
                            System.out.println("msg：2" + msg);
                            //Toast.makeText(a, "订单获取成功", Toast.LENGTH_LONG).show();
                            String dingdanxinxi = dataJson.getString("jsonstring");
                            System.out.println("jsonstring：" + dingdanxinxi);
                            retList = JsonListUtil.jsonToList(dingdanxinxi, UserOrder.class);
                                /*for (UserOrder uo:retList){
                                    System.out.println("从服务器下载的订单编号为："+uo.getOrderNo());
                                }*/
                            View bannerView;
                            TextView tv_orderNO;
                            TextView tv_orderStatus;
                            TextView tv_orderDate;
                            TextView tv_orderStart;
                            TextView tv_orderEnd;


                            for (int i = 0; i < retList.size(); i++) {
                                bannerView = ((LayoutInflater) context.getSystemService(Context
                                        .LAYOUT_INFLATER_SERVICE)).inflate(R
                                        .layout.my_orderitem, null);
                                tv_orderNO = bannerView.findViewById(R.id.orderNO);
                                tv_orderStatus = bannerView.findViewById(R.id.orderStatus);
                                tv_orderDate = bannerView.findViewById(R.id.orderDate);
                                tv_orderStart = bannerView.findViewById(R.id.orderStart);
                                tv_orderEnd = bannerView.findViewById(R.id.orderEnd);
                                //two_2.addView(tv1);
                                //Gson gson=new Gson();
                                if (null == retList || retList.size() == 0) {
                                    Toast.makeText(a, "程序异常", Toast.LENGTH_LONG).show();
                                } else if (retList.get(i).getStatus() == 0) {
                                    tv_orderStatus.setText("待支付");
                                } else if (retList.get(i).getStatus() == 1) {
                                    tv_orderStatus.setText("待抢单");
                                } else if (retList.get(i).getStatus() == 2) {
                                    tv_orderStatus.setText("进行中");
                                } else if (retList.get(i).getStatus() == 3) {
                                    tv_orderStatus.setText("已完成");

                                } else {
                                    tv_orderStatus.setText("状态异常");
                                }
                                tv_orderStatus.setOnClickListener(a);
                                tv_orderNO.setText(String.valueOf(retList.get(i).getOrderNo()));

                                tv_orderStatus.setId(getids(retList.get(i).getOrderNo()));

                                tv_orderDate.setText(retList.get(i).getCommittime());
                                tv_orderStart.setText(retList.get(i).getSendlocation());
                                tv_orderEnd.setText(retList.get(i).getReceive_location());
                                System.out.println(String.valueOf(retList.get(i).getOrderNo()));
                                one.addView(bannerView);
                            }
                        } else if (successStatus == true && msg.equals("0")) {
                            Toast.makeText(a, "暂无订单数据", Toast.LENGTH_LONG).show();
                        } else {
                            System.out.println("msg：1" + msg);
                            Intent intent = new Intent();
                            intent.setClass(context, LoginActivity.class);
                            a.startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(a, "程序异常", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> arg0, Throwable t) {
                Toast.makeText(a, "网络异常", Toast.LENGTH_LONG).show();
                //请求失败操作
                System.out.println("请求失败操作1：*****" + t.getMessage());
                System.out.println("请求失败操作2：*****" + t.fillInStackTrace().getCause());
                System.out.println("请求失败操作3：*****" + t.fillInStackTrace().getLocalizedMessage());
                //System.out.println("请求失败操作4：*****"+t.getCause().toString());
                System.out.println("请求失败操作5：*****" + t.getStackTrace().toString());
                System.out.println("请求失败操作6：*****" + arg0.request().url().toString());
                // t.getMessage();


                t.fillInStackTrace().printStackTrace();
            }
        });


        return "1";
    }

    public static Call<ResponseBody> getCall(OrderService service, int num, RequestBody body) {
        switch (num) {
            case 3:
                return service.getOrderDetail(body);

        }
        return service.getOrderDetail(body);
    }

    public static String getOrderDetail(final Context context, final myOrde a, int num, int
            orderNo, final String Stringa) {
        final OrderServiceImpl osi = new OrderServiceImpl();
        String Stringx = osi.getStr(orderNo);
        if (osi.getStr(orderNo).length() > 0) {
            token = SharedPreferencesHelper.getInstance(context).getStringValue("token");
            Properties properties = AppProperties.getProperties(context);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;" +
                    "charset=UTF-8"), Stringx);
            OrderService service = provideClientApi(properties.getProperty("serverUrl")).create
                    (OrderService.class);
            Call<ResponseBody> call = getCall(service, num, body);
            final String[] updateToken = {""};
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    updateToken[0] = response.headers().get("token");
                    SharedPreferencesHelper.getInstance(context).putStringValue("token",
                            updateToken[0]);
                    String jsonString = "";
                    try {
                        jsonString = response.body().string();
                        if (null == jsonString || "".equals(jsonString)) {

                            Toast.makeText(a, "网络异常", Toast.LENGTH_LONG).show();
                        } else {

                            JSONObject dataJson = new JSONObject(jsonString);
                            String msg = dataJson.getString("msg");
                            System.out.println("msg：" + msg);
                            boolean successStatus = dataJson.getBoolean("success");


                            if (successStatus == true && msg.equals("ok")) {
                                System.out.println("msg：2" + msg);
                                String dingdanxinxi = dataJson.getString("jsonstringa");
                                String jsonstringb = dataJson.getString("jsonstringb");
                                osi.onAccessSuccess(dingdanxinxi, a, jsonstringb);
                                //  System.out.println("jsonstring：" + dingdanxinxi);

                            } else if (successStatus == true && msg.equals("0")) {
                                Toast.makeText(a, "暂无数据", Toast.LENGTH_LONG).show();
                            } else {
                                System.out.println("msg：1" + msg);
                                Intent intent = new Intent();
                                intent.setClass(context, LoginActivity.class);
                                a.startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(a, "程序异常", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> arg0, Throwable t) {
                    Toast.makeText(a, "网络异常", Toast.LENGTH_LONG).show();
                    //请求失败操作
                    System.out.println("请求失败操作1：*****" + t.getMessage());
                    System.out.println("请求失败操作2：*****" + t.fillInStackTrace().getCause());
                    System.out.println("请求失败操作3：*****" + t.fillInStackTrace().getLocalizedMessage
                            ());
                    //System.out.println("请求失败操作4：*****"+t.getCause().toString());
                    System.out.println("请求失败操作5：*****" + t.getStackTrace().toString());
                    System.out.println("请求失败操作6：*****" + arg0.request().url().toString());
                    // t.getMessage();


                    t.fillInStackTrace().printStackTrace();
                }
            });


            return "1";
        }
        return "1";
    }
}