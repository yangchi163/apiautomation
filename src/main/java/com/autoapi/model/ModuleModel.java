package com.autoapi.model;

import java.util.HashMap;
import java.util.Map;

public class ModuleModel extends BaseModel{
    private Map<String,ApiModel> apis = new HashMap<String, ApiModel>();

    public ModuleModel(String name) {
        this.name = name;
    }

    public Map<String, ApiModel> getApis() {
        return apis;
    }

    public void setApis(Map<String, ApiModel> apis) {
        this.apis = apis;
    }

    @Override
    public String toString() {
        return "ModuleModel{" +
                "apis=" + apis +
                ", name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                ", sonNumber=" + sonNumber +
                ", sonHasRunNumber=" + sonHasRunNumber +
                ", run=" + run +
                '}';
    }
}
