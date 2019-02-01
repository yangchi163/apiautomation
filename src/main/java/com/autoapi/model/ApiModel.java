package com.autoapi.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApiModel extends BaseModel{
    private RequestModel requestModel;
    private Map<String,CaseModel> cases = new HashMap<String, CaseModel>();

    public ApiModel(String name) {
        this.name = name;
    }

    public ApiModel(String name,String[] path) {
        this(name);
        this.path = path;
    }

    public RequestModel getRequestModel() {
        return requestModel;
    }

    public void setRequestModel(RequestModel requestModel) {
        this.requestModel = requestModel;
    }

    public Map<String, CaseModel> getCases() {
        return cases;
    }

    public void setCases(Map<String, CaseModel> cases) {
        this.cases = cases;
    }

    @Override
    public String toString() {
        return "ApiModel{" +
                "requestModel=" + requestModel +
                ", cases=" + cases +
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
