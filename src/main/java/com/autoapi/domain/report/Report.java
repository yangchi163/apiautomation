package com.autoapi.domain.report;

import com.autoapi.model.ApiConfig;
import com.google.gson.Gson;

import java.io.*;

public class Report {
    public void saveReport(ApiConfig apiConfig){
        String fileName = System.getProperty("user.dir") + File.separator +"report" + File.separator +"data.json";
        Gson gson = new Gson();
        String jsonString = gson.toJson(apiConfig);
        saveDataToFile(fileName,jsonString);
    }

    private void saveDataToFile(String fileName,String data) {
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
