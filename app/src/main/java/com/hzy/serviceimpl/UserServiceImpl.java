package com.hzy.serviceimpl;

import com.sun.jersey.api.client.Client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.MediaType;

/**
 * Created by hzy on 2018/1/23.
 */

public class UserServiceImpl {
    public String login(String username, String password) {
        // System.out.println("****************************");
        // udi.add();
        // System.out.println("****************************");

       // UserInfo u = udi.getUserByName(username);

        System.out.println("username:"+username+"password:"+password);
        password = this.jiami(password);

         System.out.println("加密后的密码为："+password);

        System.out.println("开始测试");
        String url= "http://172.25.220.8:8080/arest/services/test/handleTest";
        //String url = "http://172.28.234.9:8080/arest/services/test/handleTest";
        // String url = "http://192.168.1.3:8080/arest/services/test/handleTest";
        // String url = "http://49.52.18.101:8080/arest/services/test/aaa?params="+;

        Client client=Client.create();




        String responce=Client.create().resource(url).type(MediaType.APPLICATION_JSON).post(String.class,username+":"+password);
        System.out.println("client返回值"+responce);

        if (null == responce) {
            System.out.println("登录失败**************  responce为null  **********************************");
            return "3";

        } else{

            return responce;
        }
    }


    public String jiami(String password) {
        // TODO Auto-generated method stub
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte b[] = md.digest();
            int x;
            StringBuffer buf = new StringBuffer("");
            for (int i = 0; i < b.length; i++) {
                x = b[i];
                if (x < 0)
                    x += 256;
                if (x < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(x));
            }
            password = buf.toString();
            // System.out.println("32位加密后的字符串: " + buf.toString());// 32位的加密
            // System.out.println("16位加密后的字符串: " + buf.toString().substring(8,
            // 24));// 16位的加密

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return password;

    }


}
