package com.autoapi.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ParseDirecotory {
    private List<String[]> list = new ArrayList();
    public String basePath = System.getProperty("user.dir") + File.separator +"src"
            + File.separator + "main" + File.separator + "resources" + File.separator + "projects";

    public ParseDirecotory() {
    }

    public ParseDirecotory(String basePath) {
        this.basePath = basePath;
    }

    /**
     * 返回的是/src/main/resources下文件排列的列表,一个数组代表一个文件，数组内数据代表路径
     *[[a,b],[],……]
     * @return
     */
    public List<String[]> listFiles(){
        return listFiles(basePath,basePath);
    }

    /**
     * @param dirPath
     * @return
     */
    private List<String[]> listFiles(String dirPath,String basePath){
        File file = new File(dirPath);
        if (file.exists()){
            File[] files = file.listFiles();
            if (files.length > 0){
                for (File f: files){
                    list.add(f.getAbsolutePath().replace(basePath+File.separator,"")
                            .split(File.separator+File.separator));
                    if (f.isDirectory()){
                        listFiles(f.getAbsolutePath(),basePath);

                    }
                }
            }
        }
        return list;
    }



}
