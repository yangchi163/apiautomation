package com.autoapi.model;

import java.util.Map;

public class CaseModel {
    private Map<String,String> var;
    private FixtureModel setup;
    private FixtureModel teardown;
    private RequestModel request;
    private AssertModel asserts;

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
}
