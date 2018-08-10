package com.autoapi.model;

import java.util.Map;

public class FunctionModel {
    //只在fixture里有
    private String functionName;
    //接受到的是linkedhashmap，参数是有序的
    private Map params;
    private String output;

    public FunctionModel() {
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "FunctionModel{" +
                "functionName='" + functionName + '\'' +
                ", params=" + params +
                ", output='" + output + '\'' +
                '}';
    }
}
