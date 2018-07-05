package com.autoapi.model;

import java.util.List;
import java.util.Map;

public class FixtureModel {
    private Map<String,ApiModel> apis;
    private List<SqlModel> sql;

    public Map<String, ApiModel> getApis() {
        return apis;
    }

    public void setApis(Map<String, ApiModel> apis) {
        this.apis = apis;
    }

    public List<SqlModel> getSql() {
        return sql;
    }

    public void setSql(List<SqlModel> sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "FixtureModel{" +
                "apis=" + apis +
                ", sql=" + sql +
                '}';
    }
}
