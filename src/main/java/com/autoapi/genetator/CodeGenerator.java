package com.autoapi.genetator;

import com.autoapi.genetator.element.AnnotationField;
import com.autoapi.genetator.element.ElementAnnotation;
import com.autoapi.genetator.element.ElementField;
import com.autoapi.genetator.element.ElementMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.autoapi.util.CommonUtil.writeToFile;

public class CodeGenerator {
    private StringBuffer code = new StringBuffer();
    //包名
    private String packageName;
    //依赖,需import
    private List<String> relyOn = new ArrayList<String>();
    //类名
    private String className;
    //父类名
    private String superClassName;
    //接口名
    private List<String> interfaceNames = new ArrayList<String>();
    //属性
    private List<ElementField> fields = new ArrayList<ElementField>();
    //方法
    private List<ElementMethod> methods = new ArrayList<ElementMethod>();

    /**
     * 生成java文件
     */
    public void generateFile(){
        //生成code字符串
        generateCode();
        String basePath = System.getProperty("user.dir") + File.separator +"src"
                + File.separator + "test" + File.separator + "java";
        String pakagePath = packageName.replaceAll("\\.","\\\\");
        String fileName = basePath + File.separator + pakagePath + File.separator + className + ".java";
        writeToFile(fileName,code.toString());
    }

    /**
     * 解析元素
     */
    private void generateCode(){
        generatePackageName();
        generateRelyOn();
        generateClassName();
        generateFields();
        generateMethods();
        generateFinish();
    }

    private void generatePackageName(){
        code.append("package " +packageName + ";" + "\r\n\r\n");
    }

    private void generateRelyOn(){
        Iterator<String> iterator = relyOn.iterator();
        while (iterator.hasNext()){
            code.append("import " + iterator.next() + ";" + "\r\n");
        }
        code.append("\r\n");
    }

    /**
     * 此处需要生成className,superClassName,interfaceName,但是没有结尾的括号
     * 例：public ClassName extends superClassName implements A,B{
     */
    private void generateClassName(){
        //添加类名
        code.append("public class " + className + " ");
        //处理父类
        if (superClassName != null && !superClassName.equals("")){
            code.append("extends " + superClassName + " ");
        }
        //处理接口
        Iterator<String> it = interfaceNames.iterator();
        if (it.hasNext()){
            code.append("implements " + it.next() + " ");
            while (it.hasNext()){
                code.append("," + it.next() + " ");
            }
        }
        //添加“{”
        code.append("{" + "\r\n");

    }

    private void generateFields(){
        Iterator<ElementField> it = fields.iterator();
        while (it.hasNext()){
            ElementField f = it.next();
            code.append("\t" + f.getPermission() + " " + f.getDataType() + " " + f.getName());
            //判断有没有赋初始值
            if (f.getValue() != null){
                code.append(" =" + f.getValue());
            }
            code.append(";" + "\r\n");

        }
    }

    private void generateMethods(){
        Iterator<ElementMethod> it = methods.iterator();
        while (it.hasNext()){
            ElementMethod m = it.next();
            //处理annotation
            Iterator<ElementAnnotation> anno = m.getAnnotations().iterator();
            while (anno.hasNext()){
                ElementAnnotation a = anno.next();
                code.append("\t@" + a.getName() + "\r\n");
                //判断注解有没有属性
                List<AnnotationField> fields = a.getFields();
                Iterator<AnnotationField> annotationFieldIterator = fields.iterator();
                if (annotationFieldIterator.hasNext()){
                    code.append("(");
                    StringBuffer temp = new StringBuffer();
                    while (annotationFieldIterator.hasNext()){
                        AnnotationField f = annotationFieldIterator.next();
                        if ("".equals(temp.toString())){
                            temp.append(f.getName() + " = " + "\"" + f.getValue() + "\"");
                        }else {
                            temp.append("," + f.getName() + " = " + "\"" + f.getValue() + "\"");
                        }
                    }
                    code.append(temp.toString() + ")");
                }
            }
            //处理方法
            code.append("\t" + m.getPermission() + " " + m.getReturnType() + " " + m.getName() + "(");
            //处理参数,并补齐“) throws Exception {”
            code.append(m.getParameters() + ") throws Exception {" + "\r\n");
            //处理方法体
            code.append(m.getBody() + "\r\n");
            //处理结尾“}”
            code.append("\t}" + "\r\n");
        }
    }

    private void generateFinish(){
        code.append("}");
    }

    /**
     * 添加元素
     */
    public void addRelyOn(String imports){
        relyOn.add(imports);
    }

    public void addInterfaceNames(String interfaceName){
        interfaceNames.add(interfaceName);
    }

    public void addField(ElementField field){
        fields.add(field);
    }

    public void addMethod(ElementMethod method){
        methods.add(method);
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSuperClassName(String superClassName) {
        this.superClassName = superClassName;
    }
}
