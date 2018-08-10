package com.autoapi.model.asserts;

public class AssertModel {
    //预期结果json：map;response:responsemodel;sql:sqlmodel
    private AssertNodeModel expect;
    //实际结果
    private AssertNodeModel actual;

    public AssertNodeModel getExpect() {
        return expect;
    }

    public void setExpect(AssertNodeModel expect) {
        this.expect = expect;
    }

    public AssertNodeModel getActual() {
        return actual;
    }

    public void setActual(AssertNodeModel actual) {
        this.actual = actual;
    }

    @Override
    public String toString() {
        return "AssertModel{" +
                "expect=" + expect +
                ", actual=" + actual +
                '}';
    }
}
