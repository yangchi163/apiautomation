package com.autoapi.domain;

import com.autoapi.domain.asserts.AssertsRun;
import com.autoapi.domain.report.Report;
import com.autoapi.getcase.CaseFactory;
import com.autoapi.model.*;
import org.apache.ibatis.io.Resources;
import java.io.InputStream;
import java.util.Map;

import static com.autoapi.keywords.FileKeyWords.YAML;
import static com.autoapi.keywords.RequestKeyWords.*;

public class ApiRun {
    //mybatis文件
    private static InputStream sqlInputStream;
    //整个用例树
    public static ApiConfig apiConfig;
    private static RunUtil runUtil;

    static {
        try {
            //加载mybatis配置
            String resource = "mybatis/mabatis-config.xml";
            sqlInputStream = Resources.getResourceAsStream(resource);
            //解析apiconfig
            apiConfig = CaseFactory.getCase(YAML);
            runUtil = new RunUtil(apiConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单独执行用例（不触发其他方法，如setup,teardown等），function,setup,teardown,casemodel等
     * @param functionPath 表示要执行的节点：project.module.setup
     */
    public static void runSingle(String functionPath) throws Exception {
        String[] casePath = functionPath.split("\\.");
        //判断执行的是setup,teardown，还是测试方法
        String lastString = casePath[casePath.length -1];
        if (SETUP.equals(lastString) || TEARDOWN.equals(lastString)){
            String[] tempPath = new String[casePath.length - 1];
            for (int i = 0; i<tempPath.length ;i++){
                tempPath[i] = casePath[i];
            }
            BaseModel model = getModel(tempPath);
            FixtureModel fixtureModel ;
            if (SETUP.equals(lastString)){
                fixtureModel = model.getSetup();
            }else {
                fixtureModel = model.getTeardown();
            }
            //执行变量替换
            runUtil.replaceVar(model.getVar(),tempPath);
            //执行setup
            runUtil.runFixture(fixtureModel,model,sqlInputStream,tempPath);

        }else {
            CaseModel caseModel = (CaseModel) getModel(casePath);
            //执行变量替换
            runUtil.replaceVar(caseModel.getVar(),casePath);
            //执行setup
            FixtureModel caseSetup = caseModel.getSetup();
            runUtil.runFixture(caseSetup,caseModel,sqlInputStream,casePath);
            //执行请求
            //此处后一个basemodel不起作用
            runUtil.runApi(caseModel,caseModel,casePath,true);

            //执行断言
            AssertsRun assertsRun = new AssertsRun();
            assert assertsRun.runAsserts(caseModel.getAsserts(),caseModel,sqlInputStream,apiConfig,casePath);
        }
    }

    /**
     *
     * @param casePath 路径 [project,module,api,case]
     * @return
     */
    private static BaseModel getModel(String[] casePath){
        String projectName;
        String moduleName;
        String apiName;
        String caseName;
        BaseModel result = null;
        ProjectModel projectModel = null;
        ModuleModel moduleModel = null;
        ApiModel apiModel = null;
        CaseModel caseModel = null;
        if (casePath.length > 0){
            projectName = casePath[0];
            Map<String,ProjectModel> projectModelMap = apiConfig.getProjects();
            for (Object key : projectModelMap.keySet()){
                if (key.equals(projectName)){
                    projectModel = projectModelMap.get(key);
                    result = projectModel;
                }
            }

        }
        if (casePath.length > 1){
            moduleName = casePath[1];
            Map<String,ModuleModel> moduleModelMap = projectModel.getModules();
            for (Object key : moduleModelMap.keySet()){
                if (key.equals(moduleName)){
                    moduleModel = moduleModelMap.get(key);
                    result = moduleModel;
                }
            }

        }
        if (casePath.length > 2){
            apiName = casePath[2];
            Map<String,ApiModel> apiModelMap = moduleModel.getApis();
            for (Object key : apiModelMap.keySet()){
                if (key.equals(apiName)){
                    apiModel = apiModelMap.get(key);
                    result = apiModel;
                }
            }
        }
        if (casePath.length > 3){
            caseName = casePath[3];
            Map<String,CaseModel> caseModelMap = apiModel.getCases();
            for (Object key : caseModelMap.keySet()){
                if (key.equals(caseName)){
                    caseModel = caseModelMap.get(key);
                    result = caseModel;
                }
            }
        }
        return result;
    }


    /**
     * 执行标记后的用例,弃用
     * @param casePath 用例路径，可变参数
     * @throws Exception
     */
    private static void run(String... casePath) throws Exception {
        //加载mybatis配置
        String resource = "mybatis/mabatis-config.xml";
        sqlInputStream = Resources.getResourceAsStream(resource);
        //解析apiconfig
        CaseFactory caseFactory = new CaseFactory();
        apiConfig = caseFactory.getCase(YAML);
        runUtil = new RunUtil(apiConfig);
        //标记要执行的用例
        collectCase(casePath);
        //执行用例
        doRun();
        //生成测试报告的数据
        Report report = new Report();
        report.saveReport(apiConfig);
    }

    /**
     * 标记哪些用例需要执行，默认是全部执行
     * @param casePath 用例路径，可变参数
     * @throws Exception
     */
    private static void collectCase(String... casePath) throws Exception {
        String projectName;
        String moduleName;
        String apiName;
        String caseName;
        ProjectModel projectModel = null;
        ModuleModel moduleModel = null;
        ApiModel apiModel = null;
        if (casePath.length > 0){
            projectName = casePath[0];
            Map<String,ProjectModel> projectModelMap = apiConfig.getProjects();
            for (Object key : projectModelMap.keySet()){
                if (key.equals(projectName)){
                    projectModel = projectModelMap.get(key);
                } else {
                    projectModelMap.get(key).setRun(false);
                }
            }

        }
        if (casePath.length > 1){
            moduleName = casePath[1];
            Map<String,ModuleModel> moduleModelMap = projectModel.getModules();
            for (Object key : moduleModelMap.keySet()){
                if (key.equals(moduleName)){
                    moduleModel = moduleModelMap.get(key);
                } else {
                    moduleModelMap.get(key).setRun(false);
                }
            }

        }
        if (casePath.length > 2){
            apiName = casePath[2];
            Map<String,ApiModel> apiModelMap = moduleModel.getApis();
            for (Object key : apiModelMap.keySet()){
                if (key.equals(apiName)){
                    apiModel = apiModelMap.get(key);
                } else {
                    apiModelMap.get(key).setRun(false);
                }
            }
        }
        if (casePath.length > 3){
            caseName = casePath[3];
            Map<String,CaseModel> caseModelMap = apiModel.getCases();
            for (Object key : caseModelMap.keySet()){
                if (!key.equals(caseName)){
                    caseModelMap.get(key).setRun(false);
                }
            }
        }



    }



    /**
     *真正执行用例的方法,由run()调用
     * @throws Exception
     */
    private static void doRun() throws Exception {
        Map<String,ProjectModel> projectModelMap = apiConfig.getProjects();

        //project层
        for (Object keyProject : projectModelMap.keySet()){
            ProjectModel projectModel = projectModelMap.get(keyProject);
            if (projectModel.isRun()){

                String[] projectPath = {(String) keyProject};
                try {
                    //执行替换变量,此时只替换var中的变量,项目为最外层，可以不执行
                    //runUtil.replaceVar(projectModel.getVar(),projectPath);
                    //执行setup
                    FixtureModel projectSetup = projectModel.getSetup();
                    runUtil.runFixture(projectSetup,projectModel,sqlInputStream,projectPath);

                    Map<String,ModuleModel> moduleModelMap = projectModel.getModules();
                    for (Object keyModule : moduleModelMap.keySet()){
                        //module层
                        ModuleModel moduleModel = moduleModelMap.get(keyModule);
                        if (moduleModel.isRun()){
                            String[] modulePath = {(String) keyProject, (String) keyModule};
                            try {
                                //执行变量替换
                                runUtil.replaceVar(moduleModel.getVar(),modulePath);
                                //执行setup
                                FixtureModel moduleSetup = moduleModel.getSetup();
                                runUtil.runFixture(moduleSetup,moduleModel,sqlInputStream,modulePath);

                                Map<String,ApiModel> apiModelMap = moduleModel.getApis();
                                for (Object keyApi : apiModelMap.keySet()){
                                    //api层
                                    ApiModel apiModel = apiModelMap.get(keyApi);
                                    if (apiModel.isRun()){
                                        String[] apiPath = {(String) keyProject, (String) keyModule, (String) keyApi};
                                        try {
                                            //执行变量替换
                                            runUtil.replaceVar(apiModel.getVar(),apiPath);
                                            //执行setup
                                            FixtureModel apiSetup = apiModel.getSetup();
                                            runUtil.runFixture(apiSetup,apiModel,sqlInputStream,apiPath);

                                            Map<String,CaseModel> caseModelMap = apiModel.getCases();
                                            for (Object keyCase:caseModelMap.keySet()){
                                                //case层
                                                CaseModel caseModel = caseModelMap.get(keyCase);
                                                if (caseModel.isRun()){
                                                    String[] casePath = {(String) keyProject, (String) keyModule, (String) keyApi, (String) keyCase};
                                                    try {
                                                        //执行变量替换
                                                        runUtil.replaceVar(caseModel.getVar(),casePath);
                                                        //执行setup
                                                        FixtureModel caseSetup = caseModel.getSetup();
                                                        runUtil.runFixture(caseSetup,caseModel,sqlInputStream,casePath);
                                                        //执行请求
                                                        //此处后一个basemodel不起作用
                                                        runUtil.runApi(caseModel,caseModel,casePath,true);

                                                        //执行断言
                                                        AssertsRun assertsRun = new AssertsRun();
                                                        assert assertsRun.runAsserts(caseModel.getAsserts(),caseModel,sqlInputStream,apiConfig,casePath);
                                                        //执行到此处时，统计执行结果
                                                        caseModel.setResult(true);
                                                        runUtil.countResult(true,caseModel,apiModel,moduleModel,projectModel);
                                                    }catch (Throwable e){
                                                        caseModel.setResult(false);
                                                        runUtil.countResult(false,caseModel,apiModel,moduleModel,projectModel);
                                                        e.printStackTrace();
                                                    }finally {
                                                        //执行teardown
                                                        FixtureModel caseTeardown = caseModel.getTeardown();
                                                        runUtil.runFixture(caseTeardown,caseModel,sqlInputStream,casePath);
                                                    }
                                                }
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        } finally {
                                            //执行teardown
                                            FixtureModel apiTeardown = apiModel.getTeardown();
                                            runUtil.runFixture(apiTeardown,apiModel,sqlInputStream,apiPath);
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                //执行tearDown
                                FixtureModel moduleTeardown = moduleModel.getTeardown();
                                runUtil.runFixture(moduleTeardown,moduleModel,sqlInputStream,modulePath);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    //执行teardown
                    FixtureModel projectTeardown = projectModel.getTeardown();
                    runUtil.runFixture(projectTeardown,projectModel,sqlInputStream,projectPath);
                }

            }
        }
    }
}
