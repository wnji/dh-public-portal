package com.blkchainsolutions.common;

import com.google.gson.Gson;

/**
 *
 * @Description : json转换
 * @Author : aven
 * @Date : 2020/2/9 2:05 下午
*/
public class JSONHelper {
    public static String toJsonString(Object obj){

        return new Gson().toJson(obj);
    }

    public static Object toObject(String json,Class clazz){
        return new Gson().fromJson(json,clazz);
    }
}
