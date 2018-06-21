package com.autoapi.util;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class YamlUtil {
    public static Map read(String filePath){
        Map map = null;
        Yaml yaml = new Yaml();
        File file = new File(filePath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            map = yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
