package com.autoapi.model;

import java.util.List;
import java.util.Map;

public class ApiModel {
    private Map<String,String> var;
    private FixtureModel setup;
    private FixtureModel teardown;
    private List<CaseModel> cases;

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

    public List<CaseModel> getCases() {
        return cases;
    }

    public void setCases(List<CaseModel> cases) {
        this.cases = cases;
    }
}
