package com.autoapi.model;

import java.util.*;

public class BaseModel {
    public String name;
    public Map var = new HashMap();
    public FixtureModel setup;
    public FixtureModel teardown;
    public String[] path;
    //统计执行结果
    public transient int total = 0;
    public transient int success = 0;
    public transient int fail = 0;
    //判断节点是否需要执行
    public transient boolean run = true;

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

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

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal() {
        total = total + 1;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess() {
        success = success + 1;
    }

    public int getFail() {
        return fail;
    }

    public void setFail() {
        fail = fail + 1;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                ", path=" + Arrays.toString(path) +
                ", total=" + total +
                ", success=" + success +
                ", fail=" + fail +
                ", run=" + run +
                '}';
    }
}
