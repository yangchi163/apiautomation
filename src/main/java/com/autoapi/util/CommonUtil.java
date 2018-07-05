package com.autoapi.util;

import java.util.Map;

public class CommonUtil {
    /**
     * 将对象转换成string,主要用来将yaml中的值转换成string统一处理
     * @param obj
     * @return
     */
    public static String toStr(Object obj){
        if (obj == null){
            return "";
        }
        if (obj instanceof String){
            return (String) obj;
        }else {
            return obj.toString();
        }

    }

    /**
     * 将b中的数据存到a中，重复的保留a中的数据,会对原有的map a 改动
     * @param a
     * @param b
     * @return
     */
    public static Map mergeMap(Map a,Map b){
        for (Object key : b.keySet()){
            a.put(key,b.get(key));
        }
        return a;
    }
}
