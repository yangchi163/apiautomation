package com.autoapi.model;

import java.util.Map;

public class ModuleModel {
    private String name;
    private Map<String,String> var;
    private FixtureModel setup;
    private FixtureModel teardown;
    private Map<String,ApiModel> apis;

    public Map<String, String> getVar() {
        return var;
    }

    public void setVar(Map<String, String> var) {
        this.var = var;
    }

    public FixtureModel getSetup() {
        return setup;
    }

    public void setSetup(FixtureModel setup) {
        this.setup = setup;
    }

    public FixtureModel getTeardown() {
        return teardown;
    }

    public void setTeardown(FixtureModel teardown) {
        this.teardown = teardown;
    }

    public Map<String, ApiModel> getApis() {
        return apis;
    }

    public void setApis(Map<String, ApiModel> apis) {
        this.apis = apis;
    }

    @Override
    public String toString() {
        return "ModuleModel{" +
                "name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                ", apis=" + apis +
                '}';
    }
}
