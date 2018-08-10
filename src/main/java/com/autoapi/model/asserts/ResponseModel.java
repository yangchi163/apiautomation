package com.autoapi.model.asserts;

public class ResponseModel {
    //指定response中某个节点的路径：例如:$.body.data
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "path='" + path + '\'' +
                '}';
    }
}
