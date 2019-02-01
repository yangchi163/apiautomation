package com.autoapi.genetator.element;

public class ElementField {
    //访问权限
    private String permission = ElementPermission.PUBLIC;
    //数据类型
    private String dataType;
    //变量名
    private String name;
    //初始值
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
