package com.hzy.util;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzy on 2018/7/14.
 */

public class JsonListUtil {


    /**
     * json 转 List<T>
     */
    public static <T> ArrayList<T> jsonToList(String json, Class<T> classOfT) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjs = new Gson().fromJson(json, type);

        ArrayList<T> listOfT = new ArrayList<>();
        for (JsonObject jsonObj : jsonObjs) {
            listOfT.add(new Gson().fromJson(jsonObj, classOfT));
        }

        return listOfT;
    }

    /**
     * json 转 List<T>
     */
    public static <T> T jsonToObject(String json, Class<T> classOfT) {

        Type type = new TypeToken<JsonObject>() {
        }.getType();
        JsonObject jsonObj = new Gson().fromJson(json, type);
        return new Gson().fromJson(jsonObj, classOfT);
    }

    /**
     * json 转 List<T>
     */
    public static <T> List<T> jsonToList1(String json, final Class<T> aaa) {
        Gson gson = new Gson();
        /*String json="[{\"username\":\"1893112199207\",\"orderNo\":5555,\"status\":2," +
                "\"servicename\":\"1\",\"receive_location\":\"国家电网国网新疆电力公司\"," +
                "\"CREATETIME\":\"2018-07-15 13:25:09\",\"UPDATETIME\":\"2018-07-15 13:25:09\"," +
                "\"sendlocation\":\"九州通医药(公交站)\"}]";*/

        //new TypeToken<classOfT>(){}.getType();
        List<T> listOfT = gson.fromJson(json, new TypeToken<JsonObject>() {
        }.getType());

        return listOfT;
    }


}

