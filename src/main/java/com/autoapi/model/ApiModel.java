package com.autoapi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiModel {
    private String name;
    private RequestModel requestModel;
    private Map<String,String> var = new HashMap<String, String>();
    private FixtureModel setup;
    private FixtureModel teardown;
    private Map<String,CaseModel> cases = new HashMap<String, CaseModel>();

    public ApiModel(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RequestModel getRequestModel() {
        return requestModel;
    }

    public void setRequestModel(RequestModel requestModel) {
        this.requestModel = requestModel;
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
                ", requestModel=" + requestModel +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                ", cases=" + cases +
                '}';
    }
}
