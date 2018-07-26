package com.autoapi.keywords;

import java.io.File;

public class FileKeyWords {
    //测试数据存放目录
    public static final String CASEBASEPATH = System.getProperty("user.dir") + File.separator +"src"
            + File.separator + "main" + File.separator + "resources" + File.separator + "projects";
    //filename
    public static final String RUNCONFIG = "runconfig";
    public static final String API = "api";
    //一级文件，还是二级文件
    public static final int PROJECTS = 1;    //项目级
    public static final int MODULES = 2;     //模块级
    public static final int APIS = 3;     //模块级

}
