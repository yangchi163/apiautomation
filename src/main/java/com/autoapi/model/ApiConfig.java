package com.autoapi.model;


import java.util.Map;

public class ApiConfig {
    private RunConfig runConfig;
    private Map projects;

    public RunConfig getRunConfig() {
        return runConfig;
    }

    public void setRunConfig(RunConfig runConfig) {
        this.runConfig = runConfig;
    }

    public Map getProjects() {
        return projects;
    }

    public void setProjects(Map projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "ApiConfig{" +
                "runConfig=" + runConfig +
                ", projects=" + projects +
                '}';
    }
}
