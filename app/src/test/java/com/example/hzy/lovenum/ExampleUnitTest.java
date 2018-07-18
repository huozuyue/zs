package com.example.hzy.lovenum;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hzy.model.UserOrder;
import com.hzy.service.OrderService;
import com.hzy.util.Jwt;
import com.hzy.util.RetrofitUtil;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Gson gson = new Gson();
        String a = "[{\"username\":\"18931121207\",\"orderNo\":45,\"status\":0," +
                "\"servicename\":\"0\",\"receive_location\":\"国家电网国网新疆电力公司\"," +
                "\"CREATETIME\":\"2018-07-15 13:25:09\",\"UPDATETIME\":\"2018-07-15 13:25:09\"," +
                "\"sendlocation\":\"九州通医药(公交站)\"}]";
        List<UserOrder> retList = gson.fromJson(a, new TypeToken<List<UserOrder>>() {
        }.getType());
        for (int i = 0; i < retList.size(); i++) {
            System.out.println(retList.get(i).getOrderNo());
        }
        assertEquals(4, 2 + 2);
    }

    @Test
    public void a() throws Exception {
        final String url = "http://192.168.1.4:8080/arest/services/test/";


        // 正常生成token----------------------------------------------------------------------------------------------------
        String token = null;
        Map<String, Object> payload = new HashMap<String, Object>();
        Date date = new Date();
        payload.put("uid", "291969452");// 用户id
        payload.put("iat", date.getTime());// 生成时间:当前
        payload.put("ext", date.getTime() + 2000 * 60 * 60);// 过期时间2小时
        token = Jwt.createToken(payload);
        System.out.println("生成的token是：" + token);
        //Map<String, Object> resultMap = Jwt.validToken(token);
        //System.out.println("校验结果是:" + getResult((String)resultMap.get("state")) );
        //HashMap<String,String> dataobj =  (HashMap<String,String>) resultMap.get("data");
        //System.out.println("从token中取出的payload数据是：" +dataobj.toString());
//这个测试有问题，通过TestToken测试就没有问题

        String jsonstring = "3:3";
        System.out.println("jsonstring的值为：*****" + jsonstring);


        OrderService service = RetrofitUtil.provideClientApi().create(OrderService.class);
        Call<ResponseBody> call = service.submitOrder(token, "aaaaa");

        // System.out.println("**call.toString();："+call.toString());


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


        //System.out.println("**服务端返回值："+jsonString);

        //assertEquals(4, 2 + 2);
    }
}