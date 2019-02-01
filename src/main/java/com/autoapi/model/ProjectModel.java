package com.autoapi.model;

import com.autoapi.getcase.yaml.ParseBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProjectModel extends BaseModel{
    private Map<String,ModuleModel> modules = new HashMap<String, ModuleModel>();
    //由api.yaml解析来的model
    private transient ProjectModel apiBaseModel;

    public ProjectModel() {
    }

    /**
     *
     * @param name 项目名
     * @param apiFilePath 项目配置文件api.yaml的路径
     * @throws Exception
     */
    public ProjectModel(String name,String apiFilePath) throws Exception {
        this.name = name;
        apiBaseModel = new ParseBase(apiFilePath).getApiBaseModel();
    }

    public ProjectModel(String name,String apiFilePath,String[] path) throws Exception {
        this(name,apiFilePath);
        this.path = path;
    }

    public ProjectModel getApiBaseModel() {
        return apiBaseModel;
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
                ", apiBaseModel=" + apiBaseModel +
                ", name='" + name + '\'' +
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
