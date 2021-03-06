package com.autoapi.model;

import java.util.Map;

public class SqlModel {
    //返回的结果是什么由dosql的返回类型确定
    private String sqlName;
    private String conn;
    //params 只能是单层map,不能嵌套
    private Map params;
    private String output;

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getConn() {
        return conn;
    }

    public void setConn(String conn) {
        this.conn = conn;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "SqlModel{" +
                "sqlName='" + sqlName + '\'' +
                ", conn='" + conn + '\'' +
                ", params=" + params +
                ", output='" + output + '\'' +
                '}';
    }
}
