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
     * 将b中的数据存到a中，重复的保留b中的数据
     * @param a
     * @param b
     * @return
     */
    public static Map mergeMap(Map a,Map b){
        for (Object key : b.keySet()){
            //a map中不包含key,直接添加
            if (!a.containsKey(key)){
                a.put(key,b.get(key));
            } else {
            //a map 中包含key时，且对象不是map时，直接添加;是map时,只替换map中的值，不是替换整个map
                if (!(b.get(key) instanceof Map)){
                    a.put(key,b.get(key));
                } else {
                    mergeMap((Map) a.get(key),(Map) b.get(key));
                }
            }
        }
        return a;
    }
}
