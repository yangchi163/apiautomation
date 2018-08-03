package com.autoapi.domain;

import com.autoapi.model.*;
import com.autoapi.parse.ParseApiConfig.ParseApiConfig;
import com.autoapi.parse.ParseApiConfig.ParseDirecotory;
import com.autoapi.util.CommonUtil;
import org.apache.ibatis.io.Resources;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ApiRun {
    //mybatis文件
    private InputStream sqlInputStream;
    //整个用例树
    public static ApiConfig apiConfig;


    public ApiRun() throws Exception {
        String resource = "mybatis/mabatis-config.xml";
        sqlInputStream = Resources.getResourceAsStream(resource);
        ParseDirecotory parseDirecotory = new ParseDirecotory();
        ParseApiConfig apiConfig = new ParseApiConfig(parseDirecotory.getCasePath());
        this.apiConfig = apiConfig.getApiConfig();
    }



    /**
     * 执行标记后的用例
     * @param casePath 用例路径，可变参数
     * @throws Exception
     */
    public void run(String... casePath) throws Exception {
        collectCase(casePath);
        System.out.println(this.apiConfig);
        doRun();
    }

    /**
     * 标记哪些用例需要执行，默认是全部执行
     * @param casePath 用例路径，可变参数
     * @throws Exception
     */
    public void collectCase(String... casePath) throws Exception {
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
        FixtureRun runFixture = new FixtureRun();
        Map<String,ProjectModel> projectModelMap = this.apiConfig.getProjects();
        //project层
        for (Object keyProject : projectModelMap.keySet()){
            ProjectModel projectModel = projectModelMap.get(keyProject);
            if (projectModel.isRun()){
                //执行替换变量
                String[] projectPath = {(String) keyProject};
                replaceVar(projectModel,projectPath);
                //执行setup
                FixtureModel projectSetup = projectModel.getSetup();
                runFixture.runFixture(projectSetup,projectModel,sqlInputStream);

                Map<String,ModuleModel> moduleModelMap = projectModel.getModules();
                for (Object keyModule : moduleModelMap.keySet()){
                    //module层
                    ModuleModel moduleModel = moduleModelMap.get(keyModule);
                    if (moduleModel.isRun()){
                        //执行变量替换
                        String[] modulePath = {(String) keyProject, (String) keyModule};
                        replaceVar(moduleModel,modulePath);
                        //执行setup
                        FixtureModel moduleSetup = moduleModel.getSetup();
                        runFixture.runFixture(moduleSetup,moduleModel,sqlInputStream);

                        Map<String,ApiModel> apiModelMap = moduleModel.getApis();
                        for (Object keyApi : apiModelMap.keySet()){
                            //api层
                            ApiModel apiModel = apiModelMap.get(keyApi);
                            if (apiModel.isRun()){
                                //执行变量替换
                                String[] apiPath = {(String) keyProject, (String) keyModule, (String) keyApi,};
                                replaceVar(apiModel,apiPath);
                                //执行setup
                                FixtureModel apiSetup = apiModel.getSetup();
                                runFixture.runFixture(apiSetup,apiModel,sqlInputStream);

                                Map<String,CaseModel> caseModelMap = apiModel.getCases();
                                for (Object keyCase:caseModelMap.keySet()){
                                    //case层
                                    CaseModel caseModel = caseModelMap.get(keyCase);
                                    if (caseModel.isRun()){
                                        //执行变量替换
                                        String[] casePath = {(String) keyProject, (String) keyModule, (String) keyApi, (String) keyCase};
                                        replaceVar(caseModel,casePath);
                                        //执行setup
                                        FixtureModel caseSetup = caseModel.getSetup();
                                        runFixture.runFixture(caseSetup,caseModel,sqlInputStream);

                                        //执行请求
                                        RunUtil runUtil = new RunUtil();
                                        //此处后一个basemodel不起作用
                                        runUtil.runApi(caseModel,caseModel);

                                        //执行teardown
                                        FixtureModel caseTeardown = caseModel.getTeardown();
                                        runFixture.runFixture(caseTeardown,caseModel,sqlInputStream);
                                        //执行完，给父节点执行计数+1
                                        //apiModel.setSonHasRunNumber();
                                    }

                                }
                                //执行teardown
                                FixtureModel apiTeardown = apiModel.getTeardown();
                                runFixture.runFixture(apiTeardown,apiModel,sqlInputStream);
                                //执行完，给父节点执行计数+1
                                //moduleModel.setSonHasRunNumber();
                            }

                        }
                        //执行tearDown
                        FixtureModel moduleTeardown = moduleModel.getTeardown();
                        runFixture.runFixture(moduleTeardown,moduleModel,sqlInputStream);
                        //执行完，给父节点执行计数+1
                        //projectModel.setSonHasRunNumber();
                    }

                }
                //执行teardown
                FixtureModel projectTeardown = projectModel.getTeardown();
                runFixture.runFixture(projectTeardown,projectModel,sqlInputStream);
            }
        }

    }



    /**
     *
     * @param o 需要替换的对象
     * @param varPath 当前变量的路径
     */
    private void replaceVar(Object o,String[] varPath){
        if (o != null){
            if (o instanceof Map){
                Map m = (Map) o;
                for (Object keyMap : m.keySet()){
                    if (m.get(keyMap) instanceof String){
                        String v = (String) m.get(keyMap);
                        m.put(keyMap,getReplaceString(v,varPath));
                    } else {
                        replaceVar(m.get(keyMap),varPath);
                    }
                }
            } else if (o instanceof List){
                //记录list的index
                int i = 0;
                List l = (List) o;
                for (Object ol: l){
                    if (ol instanceof String){
                        l.set(i,getReplaceString((String) ol,varPath));
                    } else {
                        replaceVar(ol,varPath);
                    }
                    i = i + 1;
                }
            } else if (o instanceof BaseModel){
                BaseModel baseModel = (BaseModel) o;
                //处理var
                replaceVar(baseModel.getVar(),varPath);
                //处理fixturemodel.setup
                replaceVar(baseModel.getSetup(),varPath);
                //处理fixturemodel.teardown
                replaceVar(baseModel.getTeardown(),varPath);
                if (o instanceof CaseModel){
                    CaseModel caseModel = (CaseModel) o;
                    //处理requstmodel
                    replaceVar(caseModel.getRequest(),varPath);
                    //处理assert
                    replaceVar(caseModel.getAsserts(),varPath);
                }
            } else if (o instanceof FixtureModel){
                FixtureModel fixtureModel = (FixtureModel) o;
                //fixture是个list
                replaceVar(fixtureModel.getFixture(),varPath);
            } else if (o instanceof RequestModel){
                RequestModel requestModel = (RequestModel) o;
                //解析urlmodel
                replaceVar(requestModel.getUrlModel(),varPath);
                //解析headers
                replaceVar(requestModel.getHeaders(),varPath);
                //解析body
                replaceVar(requestModel.getBody(),varPath);
            } else if (o instanceof UrlModel){
                UrlModel urlModel = (UrlModel) o;
                //解析params
                replaceVar(urlModel.getParams(),varPath);
                //解析path
                replaceVar(urlModel.getPath(),varPath);
            } else if (o instanceof SqlModel){
              SqlModel sqlModel = (SqlModel) o;
              //解析sql中变量
              replaceVar(sqlModel.getSql(),varPath);
            } else {
                //数字类的不做处理
            }
        }
    }

    /**
     * @param src 待替换的string
     * @param varPath 当前变量所处的路径：project-module-api-case : 没有包含varname
     * @return
     */
    private String getReplaceString(String src,String[] varPath){
        while (true){
            //s是需要替换掉的部分,s=${aaaa}
            String s = CommonUtil.getFirstString(src);
            if (s != null){
                //varname 是待替换的变量名
                String varName = s.substring(2,s.length()-1);
                String[] realVarName = {varName};
                String[] realVarPath = CommonUtil.mergeArray(varPath,realVarName);
                src = src.replace(s, (String) getReplaceValue(realVarPath));
            } else {
                break;
            }
        }
        return src;
    }


    /**
     * 获得请求数据中变量对应的值
     * @param varPath 变量对应的路径数组 project-module-api-case-varname
     * @return
     */
    private Object getReplaceValue(String[] varPath){
        Object res = null;
        String varName = varPath[varPath.length - 1];
        ProjectModel projectModel = null;
        ModuleModel moduleModel = null;
        ApiModel apiModel = null;
        CaseModel caseModel;
        if (varPath.length > 1){
            projectModel = this.apiConfig.getProjects().get(varPath[0]);
            if (projectModel.getVar().containsKey(varName)){
                res = projectModel.getVar().get(varName);
            }
        }
        if (varPath.length > 2){
            moduleModel = projectModel.getModules().get(varPath[1]);
            if (projectModel.getVar().containsKey(varName)){
                res = moduleModel.getVar().get(varName);
            }
        }
        if (varPath.length > 3){
            apiModel = moduleModel.getApis().get(varPath[2]);
            if (projectModel.getVar().containsKey(varName)){
                res = apiModel.getVar().get(varName);
            }
        }
        if (varPath.length > 4){
            caseModel = apiModel.getCases().get(varPath[3]);
            if (projectModel.getVar().containsKey(varName)){
                res = caseModel.getVar().get(varName);
            }
        }
        return res;
    }

}
