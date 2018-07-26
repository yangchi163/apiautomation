package com.autoapi.model;

import java.util.Map;

public class CaseModel {
    private String name;
    private Map<String,String> var;
    private FixtureModel setup;
    private FixtureModel teardown;
    private RequestModel request = new RequestModel("");
    private AssertModel asserts;

    public CaseModel(String name) {
        this.name = name;
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

    public RequestModel getRequest() {
        return request;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public AssertModel getAsserts() {
        return asserts;
    }

    public void setAsserts(AssertModel asserts) {
        this.asserts = asserts;
    }

    @Override
    public String toString() {
        return "CaseModel{" +
                "name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                ", request=" + request +
                ", asserts=" + asserts +
                '}';
    }
}
