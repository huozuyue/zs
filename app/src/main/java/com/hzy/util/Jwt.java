package com.hzy.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import net.minidev.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hzy on 2018/7/5.
 */

public class Jwt {
    /**
     * 秘钥
     */
    private static final byte[] SECRET = "3d990d2276917dfac04467df11fff26d".getBytes();

    /**
     * 初始化head部分的数据为
     * {
     * "alg":"HS256",
     * "type":"JWT"
     * }
     */
    private static final JWSHeader header = new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT,
            null, null, null, null, null, null, null, null, null, null, null);


    public static String createToken(Map<String, Object> payload) {
        String tokenString = null;
        // 创建一个 JWS object
        JWSObject jwsObject = new JWSObject(header, new Payload(new JSONObject(payload)));
        try {
            // 将jwsObject 进行HMAC签名
            jwsObject.sign(new MACSigner(SECRET));
            tokenString = jwsObject.serialize();
        } catch (JOSEException e) {
            System.err.println("签名失败:" + e.getMessage());
            e.printStackTrace();
        }
        return tokenString;
    }

    public static String updateToken(String uid) {
        String uptokenString = null;

        Map<String, Object> updatePayload = new HashMap<String, Object>();
        Date date = new Date();
        updatePayload.put("uid", uid);// 用户id
        updatePayload.put("iat", date.getTime());// 生成时间:当前
        updatePayload.put("ext", date.getTime() + 2000 * 60 * 60);// 过期时间2小时
        uptokenString = Jwt.createToken(updatePayload);
        return uptokenString;
    }

    /**
     * 校验token是否合法，返回Map集合,集合中主要包含    state状态码   data鉴权成功后从token中提取的数据
     * 该方法在过滤器中调用，每次请求API时都校验
     *
     * @param token
     * @return Map<String, Object>
     */
    public static Map<String, Object> validToken(String token) {
        System.out.println("进入validToken方法，参数token为：" + token);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            System.out.println("从请求头中获取解析后的JWSObject：" + jwsObject.toString());
            Payload payload = jwsObject.getPayload();
            System.out.println("从请求头中获取payload：" + payload);
            JWSVerifier verifier = new MACVerifier(SECRET);
            System.out.println("从请求头中获取verifier：" + verifier.toString());

            if (jwsObject.verify(verifier)) {
                JSONObject jsonOBj = payload.toJSONObject();
                //System.out.println("从请求头中获取payload.toJSONObject()："+jsonOBj.toJSONString());
                // token校验成功（此时没有校验是否过期）
                resultMap.put("state", TokenState.VALID.toString());
                // 若payload包含ext字段，则校验是否过期
                if (jsonOBj.containsKey("ext")) {
                    long extTime = Long.valueOf(jsonOBj.get("ext").toString());

                    //System.out.println("从请求头中获取extTime："+extTime);
                    String guoqi = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format
                            (extTime);
                    System.out.println("从请求头中获取过期时间：" + guoqi);
                    long curTime = new Date().getTime();
                    //System.out.println("从请求头中获取curTime："+curTime);
                    String dangqian = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format
                            (curTime);
                    System.out.println("获取当前时间：" + dangqian);
                    // 过期了
                    if (curTime > extTime) {
                        System.out.println("curTime > extTime：token已经过期了");
                        resultMap.clear();
                        resultMap.put("state", TokenState.EXPIRED.toString());
                    }
                }
                System.out.println("校验结果：token为有效token");
                resultMap.put("data", jsonOBj);
                System.out.println("uid：" + jsonOBj.get("uid"));
                resultMap.put("uid", jsonOBj.get("uid"));
                //创建一个新token
                System.out.println("创建一个新token为：" + Jwt.updateToken((String) jsonOBj.get("uid")));
                resultMap.put("newToken", Jwt.updateToken((String) jsonOBj.get("uid")));

            } else {
                // 校验失败
                System.out.println("校验结果：token为无效token");
                resultMap.put("state", TokenState.INVALID.toString());
            }

        } catch (Exception e) {
            //e.printStackTrace();
            // token格式不合法导致的异常
            System.out.println("校验结果：token校验异常");
            resultMap.clear();
            resultMap.put("state", TokenState.INVALID.toString());
        }
        System.out.println("结束token校验方法：validToken");

        return resultMap;
    }

}
