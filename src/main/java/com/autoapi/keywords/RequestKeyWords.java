package com.autoapi.keywords;

public class RequestKeyWords {
    //字符集
    public static final String UTF8 = "utf-8";
    //传参方式
    //public static final String JSON = "json";
    public static final String FORM = "form";

    public static final String URL = "url";
    public static final String SCHEMA = "schema";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String VERSION = "version";
    public static final String METHOD = "method";
    public static final String HEADERS = "headers";
    public static final String BODY = "data";                     //为了兼容当前的配置文件
    public static final String PARAMS = "params";                 //url中，sql中
    public static final String BASE = "base";
    public static final String API = "api";
    public static final String PATH = "path";
    public static final String VAR = "var";
    public static final String TESTCASE = "test-case";
    //断言模块
    public static final String ASSERTS = "asserts";
    public static final String JSON = "json";
    public static final String EXPECT = "expexct";
    public static final String ACTUAL = "actual";
    //fixture模块key
    public static final String SETUP = "setup";
    public static final String TEARDOWN = "teardown";
    public static final String SQL = "sql";
    public static final String FUNCTION = "function";
    public static final String CONN = "conn";
    public static final String RES = "res";
    public static final String RESLIST = "resList";
    public static final String OUTPUT = "output";

    //请求方法
    public static final String PUT = "put";
    public static final String DELETE = "delete";
    public static final String POST = "post";
    public static final String GET = "get";
    //var中的保留变量
    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
}
