package com.autoapi.model.asserts;

public class AssertModel {
    //预期结果表达式json：map;response:responsemodel;sql:sqlmodel
    private AssertNodeModel expect;
    //实际结果表达式
    private AssertNodeModel actual;
    //预期结果
    private Object expectObj;
    //实际结果
    private Object actualObj;

    public Object getExpectObj() {
        return expectObj;
    }

    public void setExpectObj(Object expectObj) {
        this.expectObj = expectObj;
    }

    public Object getActualObj() {
        return actualObj;
    }

    public void setActualObj(Object actualObj) {
        this.actualObj = actualObj;
    }

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
                ", expectObj=" + expectObj +
                ", actualObj=" + actualObj +
                '}';
    }
}
