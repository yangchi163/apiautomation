package com.autoapi.domain;

import com.autoapi.domain.asserts.AssertsRun;
import com.autoapi.domain.report.Report;
import com.autoapi.model.*;
import com.autoapi.parse.ParseApiConfig.ParseApiConfig;
import com.autoapi.parse.ParseApiConfig.ParseDirecotory;
import org.apache.ibatis.io.Resources;

import java.io.InputStream;
import java.util.Map;

public class ApiRun {
    //mybatis文件
    private InputStream sqlInputStream;
    //整个用例树
    public static ApiConfig apiConfig;
    private RunUtil runUtil;


    public ApiRun() throws Exception {
    }



    /**
     * 执行标记后的用例
     * @param casePath 用例路径，可变参数
     * @throws Exception
     */
    public void run(String... casePath) throws Exception {
        //加载mybatis配置
        String resource = "mybatis/mabatis-config.xml";
        sqlInputStream = Resources.getResourceAsStream(resource);
        //解析apiconfig
        ParseDirecotory parseDirecotory = new ParseDirecotory();
        ParseApiConfig parseApiConfig = new ParseApiConfig(parseDirecotory.getCasePath());
        this.apiConfig = parseApiConfig.getApiConfig();
        runUtil = new RunUtil(apiConfig);
        //标记要执行的用例
        collectCase(casePath);
        //执行用例
        doRun();
        //生成测试报告的数据
        Report report = new Report();
        report.saveReport(this.apiConfig);
    }

    /**
     * 标记哪些用例需要执行，默认是全部执行
     * @param casePath 用例路径，可变参数
     * @throws Exception
     */
    private void collectCase(String... casePath) throws Exception {
        String projectName;
        String moduleName;
        String apiName;
        String caseName;
        ProjectModel projectModel = null;
        ModuleModel moduleModel = null;
        ApiModel apiModel = null;
        if (casePath.length > 0){
            projectName = casePath[0];
            Map<String,ProjectModel> projectModelMap = this.apiConfig.getProjects();
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
    private void doRun() throws Exception {
        Map<String,ProjectModel> projectModelMap = this.apiConfig.getProjects();

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
                                                        runUtil.runApi(caseModel,caseModel,casePath);

                                                        //执行断言
                                                        AssertsRun assertsRun = new AssertsRun();
                                                        assert assertsRun.runAsserts(caseModel.getAsserts(),caseModel,sqlInputStream,apiConfig,casePath);
                                                        //执行到此处时，更改执行结果为成功
                                                        caseModel.setResult(true);

                                                    }catch (Throwable e){
                                                        caseModel.setResult(false);
                                                        e.printStackTrace();
                                                    }finally {
                                                        //执行teardown
                                                        FixtureModel caseTeardown = caseModel.getTeardown();
                                                        runUtil.runFixture(caseTeardown,caseModel,sqlInputStream,casePath);
                                                        //执行完，给父节点执行计数+1
                                                        //apiModel.setSonHasRunNumber();
                                                    }

                                                }

                                            }

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        } finally {
                                            //执行teardown
                                            FixtureModel apiTeardown = apiModel.getTeardown();
                                            runUtil.runFixture(apiTeardown,apiModel,sqlInputStream,apiPath);
                                            //执行完，给父节点执行计数+1
                                            //moduleModel.setSonHasRunNumber();
                                        }

                                    }

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                //执行tearDown
                                FixtureModel moduleTeardown = moduleModel.getTeardown();
                                runUtil.runFixture(moduleTeardown,moduleModel,sqlInputStream,modulePath);
                                //执行完，给父节点执行计数+1
                                //projectModel.setSonHasRunNumber();
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
