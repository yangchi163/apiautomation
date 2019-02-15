package com.autoapi.genetator.javaelement;

import java.util.ArrayList;
import java.util.List;

public class ElementMethod {
    //访问权限
    private String permission = ElementPermission.PUBLIC;
    //返回类型
    private String returnType;
    //变量名
    private String name;
    //方法体
    private String body;
    //方法参数
    private String parameters = "";
    //注解
    private List<ElementAnnotation> annotations = new ArrayList<ElementAnnotation>();

    public ElementMethod() {
    }

    public ElementMethod(String permission, String returnType, String name, String body, String parameters, List<String> anno) {
        this.permission = permission;
        this.returnType = returnType;
        this.name = name;
        this.body = body;
        this.parameters = parameters;
        for (String s : anno){
            annotations.add(new ElementAnnotation(s));
        }
    }

    public void addAnnotation(ElementAnnotation annotation ){
        annotations.add(annotation);
    }

    public List<ElementAnnotation> getAnnotations() {
        return annotations;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
