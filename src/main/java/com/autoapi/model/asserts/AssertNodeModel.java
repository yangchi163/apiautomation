package com.autoapi.model.asserts;

public class AssertNodeModel {
    //预期结果json：map;response:responsemodel;sql:sqlmodel
    public Object assertNode;

    public AssertNodeModel() {
    }

    public Object getAssertNode() {
        return assertNode;
    }

    public void setAssertNode(Object assertNode) {
        this.assertNode = assertNode;
    }

    @Override
    public String toString() {
        return "AssertNodeModel{" +
                "assertNode=" + assertNode +
                '}';
    }
}
