package com.autoapi.domain.report;

import com.autoapi.model.ApiConfig;
import com.google.gson.Gson;

import java.io.*;

import static com.autoapi.util.CommonUtil.writeToFile;

public class Report {
    public void saveReport(ApiConfig apiConfig){
        String fileName = System.getProperty("user.dir") + File.separator +"report" + File.separator +"data.json";
        Gson gson = new Gson();
        String jsonString = gson.toJson(apiConfig);
        writeToFile(fileName,jsonString);
    }
}
