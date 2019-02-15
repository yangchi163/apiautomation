package com.autoapi.domain;

import com.autoapi.genetator.XmlGenerator;
import com.autoapi.getcase.CaseFactory;
import com.autoapi.model.ApiConfig;
import com.autoapi.model.ModuleModel;
import com.autoapi.model.ProjectModel;
import org.dom4j.Element;

import java.util.Map;

import static com.autoapi.keywords.FileKeyWords.YAML;

/**
 * 生成testng.xml
 */
public class GenerateXml {
    private ApiConfig apiConfig;

    /**
     * 生成测试用例的树结构
     * @throws Exception
     */
    public void generateApiConfig() throws Exception {
        apiConfig = CaseFactory.getCase(YAML);
    }

    public void generateXml() throws Exception {
        //初始化xml生成器
        XmlGenerator xmlGenerator = new XmlGenerator("");
        //添加根节点
        Element root = xmlGenerator.addElement("suite");
        xmlGenerator.addAttribute(root,"name","All Test Suite");

        //获得apiconfig
        generateApiConfig();
        //根据apiconfig生成测试用例文件
        if (apiConfig == null){
            throw new Exception("apiconfig is null");
        }
        //获得所有项目
        Map<String,ProjectModel> projectModelMap = this.apiConfig.getProjects();

        //project层
        for (Object keyProject : projectModelMap.keySet()){
            ProjectModel projectModel = projectModelMap.get(keyProject);
            //添加test节点，代表项目
            Element projectNode = xmlGenerator.addElement(root,"test");
            xmlGenerator.addAttribute(projectNode,"verbose","2");
            xmlGenerator.addAttribute(projectNode,"name",projectModel.getName());
            //添加packages
            Element packagesNode = xmlGenerator.addElement(projectNode,"packages");
            //添加项目对应的package
            Element packageNode = xmlGenerator.addElement(packagesNode,"package");
            xmlGenerator.addAttribute(packageNode,"name",projectModel.getName());

            //获得项目下所有模块
            Map<String,ModuleModel> moduleModelMap = projectModel.getModules();

            //module层
            for (Object keyModule : moduleModelMap.keySet()){
                ModuleModel moduleModel = moduleModelMap.get(keyModule);
                //添加pakackage节点，代表包
                Element packageNode2 = xmlGenerator.addElement(packagesNode,"package");
                xmlGenerator.addAttribute(packageNode2,"name",projectModel.getName()+ "."+moduleModel.getName());
//  暂时不需要加到api节点
//                //获得模块下所有api
//                Map<String,ApiModel> apiModelMap = moduleModel.getApis();
//
//                //api层
//                for (Object keyApi : apiModelMap.keySet()){
//                    ApiModel apiModel = apiModelMap.get(keyApi);
//
//                }
            }
        }

        xmlGenerator.genetateXml();

    }

}
