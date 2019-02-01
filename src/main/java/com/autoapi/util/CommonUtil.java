package com.autoapi.util;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;

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
        if (a == null){
            return b;
        }
        if (b != null){
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
        }
        return a;
    }

    /**
     * 短期内能满足需求，后期再优化
     * 注：${${}}不可以如此嵌套使用
     * 返回匹配到的第一个${}
     * @param s
     * @return
     */
    public static String getFirstString(String s,int beginIndex){
        int start = s.indexOf("${",beginIndex);
        int end = s.indexOf("}",start);
        if (start == -1 || end == -1){
            return null;
        }
        return s.substring(start,end+1);
    }


    /**
     * 合并2个数组，有顺序，a中元素在前
     * @param a
     * @param b
     * @return
     */
    public static String[] mergeArray(String[] a,String[] b){
        String[] res = new String[a.length+b.length];
        for (int i = 0;i<a.length;i++){
            res[i] = a[i];
        }
        for (int j = 0; j<b.length;j++){
            res[a.length + j] = b[j];
        }
        return res;
    }

    public static Map parseJsonFile(String filePath) throws IOException {
        File file=new File(filePath);
        String str = FileUtils.readFileToString(file,UTF8);
        Gson gson = new Gson();
        return gson.fromJson(str,Map.class);
    }

    /**
     * 检查拿到的值是不是变量 ${varName}
     * @param target 要检查的值
     * @return
     */
    public static boolean isVariable(Object target){
        boolean res = false;
        if (target instanceof String){
            String temp = (String) target;
            if (temp.startsWith("${") && temp.endsWith("}")){
                res = true;
            }
        }
        return res;
    }

    /**
     * 根据路径从json中返回对应值
     * @param jsonString json字符串
     * @param jsonpath json路径
     * @return
     */
    public static Object getFromJsonByjsonPath(String jsonString,String jsonpath){
        ReadContext context = JsonPath.parse(jsonString);
        Object o = context.read(jsonpath);
        return o;
    }

    /**
     * 如果key在target2中存在，就用target2中的
     * 不存在就用target1中的数据
     * 如果key对应的是map，则合并2个map后返回
     * @param target1
     * @param target2
     * @param key
     * @return
     * @throws Exception
     */
    private Object getValueFromMap(Map target1,Map target2,Object key) {
        Object res = null;
        if (target1 != null){
            if (target1.containsKey(key)){
                res = target1.get(key);
            }
        }
        if (target2 != null){
            if (target2.containsKey(key)){
                if (target2.get(key) instanceof Map){
                    res = CommonUtil.mergeMap((Map) res,(Map) target2.get(key));
                }else {
                    res = target2.get(key);
                }
            }
        }
        return res;
    }

    public static void writeToFile(String fileName,String data) {
        BufferedWriter writer = null;
        File file = new File(fileName);
        //如果文件不存在，则新建一个
        if(!file.exists()){
            try {
                //创建父目录
                if (!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                //创建文件
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件写入成功！");

    }


}
