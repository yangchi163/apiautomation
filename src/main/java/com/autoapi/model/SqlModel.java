package com.autoapi.model;

import java.util.Map;

public class SqlModel {
    //返回的结果是什么由dosql的返回类型确定
    private String sql;
    private String conn;
    private Map params;
    private String output;

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

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getConn() {
        return conn;
    }

    public void setConn(String conn) {
        this.conn = conn;
    }

    @Override
    public String toString() {
        return "SqlModel{" +
                "sql='" + sql + '\'' +
                ", conn='" + conn + '\'' +
                ", params=" + params +
                ", output='" + output + '\'' +
                '}';
    }
}
