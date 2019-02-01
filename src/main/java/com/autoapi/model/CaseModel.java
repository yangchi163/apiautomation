package com.autoapi.model;

import com.autoapi.model.asserts.AssertModel;

import java.util.Arrays;
import java.util.List;

public class CaseModel extends BaseModel{
    private RequestModel request = new RequestModel("");
    private boolean result;
    private String output;
    private List<AssertModel> asserts;

    public CaseModel(String name) {
        this.name = name;
    }

    public CaseModel(String name,String[] path) {
        this(name);
        this.path = path;
    }

    public RequestModel getRequest() {
        return request;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public List<AssertModel> getAsserts() {
        return asserts;
    }

    public void setAsserts(List<AssertModel> asserts) {
        this.asserts = asserts;
    }

    @Override
    public String toString() {
        return "CaseModel{" +
                "request=" + request +
                ", result=" + result +
                ", output='" + output + '\'' +
                ", asserts=" + asserts +
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
