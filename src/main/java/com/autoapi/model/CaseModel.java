package com.autoapi.model;

public class CaseModel extends BaseModel{
    private RequestModel request = new RequestModel("");
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

    @Override
    public String toString() {
        return "CaseModel{" +
                "request=" + request +
                ", asserts=" + asserts +
                ", name='" + name + '\'' +
                ", var=" + var +
                ", setup=" + setup +
                ", teardown=" + teardown +
                '}';
    }
}
