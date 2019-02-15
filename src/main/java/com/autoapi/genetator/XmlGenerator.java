package com.autoapi.genetator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;

public class XmlGenerator {
    //document对象
    private Document document;

    private String filePath;

    private final String fileName = "testng.xml";

    private final String encoding = "UTF-8";

    public XmlGenerator(String filePath) {
        this.document = DocumentHelper.createDocument();
        this.filePath = filePath;
    }

    /**
     *
     * @param element 父当前节点
     * @param name 子节点名
     */
    public Element addElement(Element element,String name){
        return element.addElement(name);
    }

    /**
     * 添加根节点
     * @param name 根节点名
     * @return
     */
    public Element addElement(String name){
        document.addDocType("suite",null,"http://testng.org/testng-1.0.dtd");
        return document.addElement(name);
    }

    /**
     *
     * @param element 节点
     * @param name 属性名
     * @param value 属性值
     */
    public void addAttribute(Element element,String name,String value){
        element.addAttribute(name,value);
    }

    public void genetateXml(){
        try {
            // 1、设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding(encoding);
            // 2、生成xml文件
            File file = new File(fileName);
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            System.out.println("生成testng.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成testng.xml失败");
        }

    }
}
