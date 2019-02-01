package com.autoapi.domain;

import com.autoapi.genetator.CodeGenerator;
import com.autoapi.genetator.element.*;
import com.autoapi.getcase.CaseFactory;
import com.autoapi.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.autoapi.keywords.FileKeyWords.MODULE;
import static com.autoapi.keywords.FileKeyWords.PROJECT;
import static com.autoapi.keywords.RequestKeyWords.*;
import static com.autoapi.keywords.FileKeyWords.YAML;

/**
 * 生成testng的测试用例
 */
public class GenetateTestCase {
    private ApiConfig apiConfig;

    /**
     * 生成测试用例的树结构
     * @throws Exception
     */
    public void generateApiConfig() throws Exception {
        apiConfig = CaseFactory.getCase(YAML);
    }

    /**
     * 生成testng的case
     */
    public void generateTestCase() throws Exception {
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
            gerateTestCaseCore(projectModel);

            //获得项目下所有模块
            Map<String,ModuleModel> moduleModelMap = projectModel.getModules();

            //module层
            for (Object keyModule : moduleModelMap.keySet()){
                ModuleModel moduleModel = moduleModelMap.get(keyModule);
                gerateTestCaseCore(moduleModel);
                //获得模块下所有api
                Map<String,ApiModel> apiModelMap = moduleModel.getApis();

                //api层
                for (Object keyApi : apiModelMap.keySet()){
                    ApiModel apiModel = apiModelMap.get(keyApi);
                    gerateTestCaseCore(apiModel);
                }
            }
        }

    }

    /**
     * 真正的执行生成testng的java文件
     * @param baseModel projectmodel,modulemode,apimodel,casemodel
     */
    private void gerateTestCaseCore(BaseModel baseModel){
        CodeGenerator codeGenerator = new CodeGenerator();
        //packageName
        String[] path = baseModel.getPath();
        StringBuilder packageName = new StringBuilder();
        for (String s : path){
            if ("".equals(packageName.toString())){
                packageName.append(s);
            }else {
                packageName.append(".");
                packageName.append(s);
            }
        }
        //className
        String className = "";

        //setup,teardown
        FixtureModel setup = baseModel.getSetup();
        FixtureModel teardown = baseModel.getTeardown();

        //针对不同的model,处理不同的方法
        if (baseModel instanceof ProjectModel){
            className = firstToUpper(PROJECT) + firstToUpper(baseModel.getName());
            if (setup != null){
                String p = packageName + "." + SETUP;
                String methodBody = generateMethodBody(p);
                List<String> annos = Arrays.asList(AnnotationName.BEFORESUITE);
                ElementMethod m = new ElementMethod(ElementPermission.PUBLIC,DataType.VOID,SETUP,methodBody,"",annos);
                codeGenerator.addMethod(m);
            }
            if (teardown != null){
                String p = packageName + "." + TEARDOWN;
                String methodBody = generateMethodBody(p);
                List<String> annos = Arrays.asList(AnnotationName.AFTERSUITE);
                ElementMethod m = new ElementMethod(ElementPermission.PUBLIC,DataType.VOID,TEARDOWN,methodBody,"",annos);
                codeGenerator.addMethod(m);
            }
        } else if (baseModel instanceof ModuleModel){
            className = firstToUpper(MODULE) + firstToUpper(baseModel.getName());
            //模块级对应groups,以后再考虑
            if (setup != null){
                String p = packageName + "." + SETUP;
                String methodBody = generateMethodBody(p);
                List<String> annos = Arrays.asList(AnnotationName.BEFOREGROUPS);
                ElementMethod m = new ElementMethod(ElementPermission.PUBLIC,DataType.VOID,SETUP,methodBody,"",annos);
                codeGenerator.addMethod(m);
            }
            if (teardown != null){
                String p = packageName + "." + TEARDOWN;
                String methodBody = generateMethodBody(p);
                List<String> annos = Arrays.asList(AnnotationName.AFTERGROUPS);
                ElementMethod m = new ElementMethod(ElementPermission.PUBLIC,DataType.VOID,TEARDOWN,methodBody,"",annos);
                codeGenerator.addMethod(m);
            }
        } else if(baseModel instanceof ApiModel){
            className = firstToUpper(baseModel.getName());
            if (setup != null){
                String p = packageName + "." + SETUP;
                String methodBody = generateMethodBody(p);
                List<String> annos = Arrays.asList(AnnotationName.BEFORECLASS);
                ElementMethod m = new ElementMethod(ElementPermission.PUBLIC,DataType.VOID,SETUP,methodBody,"",annos);
                codeGenerator.addMethod(m);
            }
            if (teardown != null){
                String p = packageName + "." + TEARDOWN;
                String methodBody = generateMethodBody(p);
                List<String> annos = Arrays.asList(AnnotationName.AFTERCLASS);
                ElementMethod m = new ElementMethod(ElementPermission.PUBLIC,DataType.VOID,TEARDOWN,methodBody,"",annos);
                codeGenerator.addMethod(m);
            }
            //casemodel需要映射成方法
            Map<String,CaseModel> cases = ((ApiModel) baseModel).getCases();
            for (Object keyCase : cases.keySet()){
                CaseModel caseModel = cases.get(keyCase);
                String p = packageName + "." + baseModel.getName() + "." + caseModel.getName();
                String methodBody = generateMethodBody(p);
                List<String> annos = Arrays.asList(AnnotationName.TEST);
                ElementMethod m = new ElementMethod(ElementPermission.PUBLIC,DataType.VOID,caseModel.getName(),methodBody,"",annos);
                codeGenerator.addMethod(m);
            }
        }
        //向codeGenerator中装填数据
        codeGenerator.setPackageName(packageName.toString());
        codeGenerator.setClassName(className);
        //导入的包
        codeGenerator.addRelyOn("org.testng.annotations.*");
        codeGenerator.addRelyOn("com.autoapi.domain.ApiRun");
        codeGenerator.generateFile();
    }

    /**
     * 将传入的字符串首字母变大写
     * @param s
     * @return
     */
    private String firstToUpper(String s){
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    /**
     * 生成方法体：形如 ApiRun.runSingle(p);
     * @param p  表示执行方法的路径
     * @return
     */
    private String generateMethodBody(String p){
        String result = "\t\t" + "ApiRun.runSingle(" + "\"" + p + "\"" + ");";
        return result;
    }

}
