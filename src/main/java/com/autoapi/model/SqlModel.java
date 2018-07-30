package com.autoapi.model;

public class SqlModel {
    private String sql;
    private String conn;
    private String res;
    private String resList;

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

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getResList() {
        return resList;
    }

    public void setResList(String resList) {
        this.resList = resList;
    }

    @Override
    public String toString() {
        return "SqlModel{" +
                "sql='" + sql + '\'' +
                ", conn='" + conn + '\'' +
                ", res='" + res + '\'' +
                ", resList='" + resList + '\'' +
                '}';
    }
}
