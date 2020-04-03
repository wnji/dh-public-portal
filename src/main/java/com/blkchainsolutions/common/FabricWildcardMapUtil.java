package com.blkchainsolutions.common;

import java.util.HashMap;
import java.util.Map;

public class FabricWildcardMapUtil {

    private final static String regexKye="$regex";
    private final static String lteKye="$lte";
    private final static String gteKye="$gte";

    public static Map getRegex(String value){
        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put(regexKye,value);
        return objectMap;
    }
    public static Map getLel(String value){
        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put(lteKye,value);
        return objectMap;
    }
    public static Map getGte(String value){
        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put(gteKye,value);
        return objectMap;
    }
}
