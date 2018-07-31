package com.autoapi.model;

import java.util.HashMap;
import java.util.Map;

public class BaseModel {
    public String name;
    public Map var = new HashMap();
    public FixtureModel setup;
    public FixtureModel teardown;
    //当前节点是否执行结束
    public int sonNumber = 0;
    public int sonHasRunNumber = 0;
    //判断节点是否需要执行
    public boolean run = true;

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

    public int getSonNumber() {
        return sonNumber;
    }

    public void setSonNumber() {
        this.sonNumber += 1;
    }

    public int getSonHasRunNumber() {
        return sonHasRunNumber;
    }

    public void setSonHasRunNumber() {
        this.sonHasRunNumber += 1;
    }

    public void setSonNumber(int sonNumber) {
        this.sonNumber = sonNumber;
    }

    public void setSonHasRunNumber(int sonHasRunNumber) {
        this.sonHasRunNumber = sonHasRunNumber;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                ", sonNumber=" + sonNumber +
                ", sonHasRunNumber=" + sonHasRunNumber +
                ", run=" + run +
                '}';
    }
}
