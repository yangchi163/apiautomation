package com.autoapi.model;

import java.util.List;
import java.util.Map;

public class ModuleModel {
    private Map<String,String> var;
    private FixtureModel setup;
    private FixtureModel teardown;
    private List<ApiModel> apis;

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

    public List<ApiModel> getApis() {
        return apis;
    }

    public void setApis(List<ApiModel> apis) {
        this.apis = apis;
    }
}
