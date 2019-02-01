package com.autoapi.genetator.element;

import java.util.ArrayList;
import java.util.List;

public class ElementAnnotation {
    //注解名
    private String name;
    //注解中的属性
    private List<AnnotationField> fields = new ArrayList<AnnotationField>();

    public ElementAnnotation() {
    }

    public void addField(AnnotationField annotationField){
        fields.add(annotationField);
    }

    public List<AnnotationField> getFields() {
        return fields;
    }

    public ElementAnnotation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
