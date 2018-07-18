package com.hzy.util;

import com.hzy.service.OrderService;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hzy on 2018/7/5.
 */

public class AddTokenRequest {
    private final static String url = "http://192.168.1.4:8080/arest/services/test/";

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // 正常生成token----------------------------------------------------------------------------------------------------
        String token = null;
        Map<String, Object> payload = new HashMap<String, Object>();
        Date date = new Date();
        payload.put("uid", "291969452");// 用户id
        payload.put("iat", date.getTime());// 生成时间:当前
        payload.put("ext", date.getTime() + 2000 * 60 * 60);// 过期时间2小时
        token = Jwt.createToken(payload);
        //System.out.println("新生成的token是：" + token+"\n马上将该token进行校验");
        //Map<String, Object> resultMap = Jwt.validToken(token);
        //System.out.println("校验结果是:" + getResult((String)resultMap.get("state")) );
        //HashMap<String,String> dataobj =  (HashMap<String,String>) resultMap.get("data");
        //System.out.println("从token中取出的payload数据是：" +dataobj.toString());


        String jsonstring = "3:3";
        System.out.println("jsonstring的值为：*****" + jsonstring);


        OrderService service = RetrofitUtil.provideClientApi().create(OrderService.class);
        Call<ResponseBody> call = service.submitOrder(token, "aaaaa");

        System.out.println("**call.toString();：" + call.toString());


        //开始异步请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("请求成功操作：*****");
                System.out.println("**url：" + call.request().url().toString());
                String updateToken = response.headers().get("token");
                System.out.println("**updateToken：" + updateToken);
                String jsonString = "";
                try {
                    jsonString = response.body().string();
                    System.out.println("**data：" + jsonString);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> arg0, Throwable t) {
                // TODO Auto-generated method stub
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


        System.out.println("**服务端返回值：");
    }


    public static String getResult(String state) {
        switch (TokenState.getTokenState(state)) {
            case VALID:

                state = "有效token";
                break;
            case EXPIRED:
                state = "过期token";
                break;
            case INVALID:
                state = "无效的token";
                break;
        }
        return state;
    }
}
