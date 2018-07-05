package com.autoapi.util;

public class CommonUtil {
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
}
