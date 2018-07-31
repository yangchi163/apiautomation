package com.autoapi.model;

import java.util.List;
import java.util.Map;

public class FixtureModel {
    //形如[{sql=[{sql=h, conn=ss}, {sql=hhe, conn=sss}]}, {sql=[{sql=h, conn=ss}, {sql=hhe, conn=sss}]}]
    private List fixture;
    private boolean hasRun = false;

    public boolean isHasRun() {
        return hasRun;
    }

    public void setHasRun(boolean hasRun) {
        this.hasRun = hasRun;
    }

    public List getFixture() {
        return fixture;
    }

    public void setFixture(List fixture) {
        this.fixture = fixture;
    }

    @Override
    public String toString() {
        return "FixtureModel{" +
                "fixture=" + fixture +
                ", hasRun=" + hasRun +
                '}';
    }
}
