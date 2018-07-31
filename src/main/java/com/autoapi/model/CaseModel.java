package com.autoapi.model;

public class CaseModel extends BaseModel{
    private RequestModel request = new RequestModel("");
    private String output;
    private AssertModel asserts;

    public CaseModel(String name) {
        this.name = name;
    }

    public RequestModel getRequest() {
        return request;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public AssertModel getAsserts() {
        return asserts;
    }

    public void setAsserts(AssertModel asserts) {
        this.asserts = asserts;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "CaseModel{" +
                "request=" + request +
                ", output='" + output + '\'' +
                ", asserts=" + asserts +
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
