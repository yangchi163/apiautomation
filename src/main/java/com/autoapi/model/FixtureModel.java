package com.autoapi.model;

import java.util.List;

public class FixtureModel {
    private List<ApiModel> apis;
    private List<SqlModel> sql;

    public List<ApiModel> getApis() {
        return apis;
    }

    public void setApis(List<ApiModel> apis) {
        this.apis = apis;
    }

    public List<SqlModel> getSql() {
        return sql;
    }

    public void setSql(List<SqlModel> sql) {
        this.sql = sql;
    }
}
