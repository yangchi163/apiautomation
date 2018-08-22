package com.autoapi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectModel extends BaseModel{
    private Map<String,ModuleModel> modules = new HashMap<String, ModuleModel>();

    public ProjectModel() {
    }

    public ProjectModel(String name) {
        this.name = name;
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
                "modules=" + modules +
                ", name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                ", total=" + total +
                ", success=" + success +
                ", fail=" + fail +
                ", sonNumber=" + sonNumber +
                ", sonHasRunNumber=" + sonHasRunNumber +
                ", run=" + run +
                '}';
    }
}
