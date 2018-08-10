package com.autoapi.model;

import java.util.List;

public class FixtureModel {
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
