package com.autoapi.model;

import java.util.HashMap;
import java.util.Map;

public class BaseModel {
    public String name;
    public Map var = new HashMap();
    public FixtureModel setup;
    public FixtureModel teardown;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getVar() {
        return var;
    }

    public void setVar(Map var) {
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

    @Override
    public String toString() {
        return "BaseModel{" +
                "name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                '}';
    }

}
