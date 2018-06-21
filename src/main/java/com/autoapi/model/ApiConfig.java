package com.autoapi.model;


import java.util.List;

public class ApiConfig {
    private RunConfig runConfig;
    private List projects;

    public RunConfig getRunConfig() {
        return runConfig;
    }

    public void setRunConfig(RunConfig runConfig) {
        this.runConfig = runConfig;
    }

    public List getProjects() {
        return projects;
    }

    public void setProjects(List projects) {
        this.projects = projects;
    }
}
