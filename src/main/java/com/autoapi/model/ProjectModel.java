package com.autoapi.model;

import java.util.List;
import java.util.Map;

public class ProjectModel {
    private Map<String,String> var;
    private FixtureModel setup;
    private FixtureModel teardown;
    private List<ModuleModel> modules;

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

    public List<ModuleModel> getModules() {
        return modules;
    }

    public void setModules(List<ModuleModel> modules) {
        this.modules = modules;
    }
}
