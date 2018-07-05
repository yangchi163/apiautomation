package com.autoapi.model;

import java.util.List;
import java.util.Map;

public class ApiModel {
    private String name;
    private Map<String,String> var;
    private FixtureModel setup;
    private FixtureModel teardown;
    private Map<String,CaseModel> cases;

    public ApiModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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

    public Map<String, CaseModel> getCases() {
        return cases;
    }

    public void setCases(Map<String, CaseModel> cases) {
        this.cases = cases;
    }

    @Override
    public String toString() {
        return "ApiModel{" +
                "name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                ", cases=" + cases +
                '}';
    }
}
