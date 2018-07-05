package com.autoapi.model;

import java.util.List;
import java.util.Map;

public class ProjectModel {
    private String name;
    private Map<String,String> var;
    private FixtureModel setup;
    private FixtureModel teardown;
    private Map<String,ModuleModel> modules;

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

    public Map<String, ModuleModel> getModules() {
        return modules;
    }

    public void setModules(Map<String, ModuleModel> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "ProjectModel{" +
                "name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                ", modules=" + modules +
                '}';
    }
}
