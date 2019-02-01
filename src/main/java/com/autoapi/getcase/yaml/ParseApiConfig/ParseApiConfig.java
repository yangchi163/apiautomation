package com.autoapi.getcase.yaml.ParseApiConfig;

import com.autoapi.keywords.FileKeyWords;
import com.autoapi.model.*;
import com.autoapi.util.YamlUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;
import static com.autoapi.keywords.FileKeyWords.*;

/**
 * 解析项目的结构  项目--模块--接口--用例
 */
public class ParseApiConfig {
    private ApiConfig apiConfig;
    private List<String[]> fileList;

    public ApiConfig getApiConfig() throws Exception {
        parseApiConfig(fileList);
        return apiConfig;
    }

    /**
     *
     * @param caseDirectory 测试用例的根目录
     * @throws Exception
     */
    public ParseApiConfig(String caseDirectory) throws Exception {
        ParseDirecotory parseDirecotory = new ParseDirecotory(caseDirectory);
        this.fileList = parseDirecotory.getCasePath();
        this.apiConfig = new ApiConfig();

    }

    public ParseApiConfig(List<String[]> fileList) {
        this.apiConfig = new ApiConfig();
        this.fileList = fileList;
    }

    /**
     *
     * @param fileList 根据配置文件的目录结构，生成测试用例的目录结构，由ParseDirecotory.getCasePath（）生成
     * @throws Exception
     */
    private void parseApiConfig(List<String[]> fileList) throws Exception {
        Map<String,ProjectModel> projects = apiConfig.getProjects();
        String projectPath = null;
        String modulePath = null;
        String apiPath = null;
        //初始化projectPath，modulePath，apiPath
        for (String[] filePath : fileList){
            if (RUNCONFIG.equals(filePath[0])){
                //runconfig.yaml单独处理
                //待处理

            }else {
                if (filePath.length == PROJECTS){
                    projectPath = filePath[0];
                    modulePath = null;
                    apiPath = null;
                }else if (filePath.length == MODULES){
                    //api.yaml单独处理
                    if (!filePath[1].equals(APIFILE)){
                        //处理模块文件夹
                        projectPath = filePath[0];
                        modulePath = filePath[1];
                        apiPath = null;
                    }
                }else if (filePath.length == APIS){
                    projectPath = filePath[0];
                    modulePath = filePath[1];
                    apiPath = filePath[2];
                }else {
                    throw new Exception("传入的fileList有误");
                }
                //组装apiconfig
                if (!projects.containsKey(projectPath)){
                    //新建项目时，需要初始化basemodel
                    String apiFilePath = FileKeyWords.CASEBASEPATH + File.separator + projectPath + File.separator + FileKeyWords.APIFILE + ".yaml";
                    projects.put(projectPath,new ProjectModel(projectPath,apiFilePath,filePath));
                }
                if (modulePath != null){
                    Map<String,ModuleModel> modules = projects.get(projectPath).getModules();
                    if (modulePath.equals(PROJECT)){
                        //单独处理project.yaml
                        parseOtherConfig(projects.get(projectPath),filePath);
                    } else {
                        if (!modules.containsKey(modulePath)){
                            modules.put(modulePath, new ModuleModel(modulePath,filePath));
                        }
                    }
                }
                if(apiPath != null){
                    Map<String,ApiModel> apis = projects.get(projectPath).getModules().get(modulePath).getApis();
                    if (apiPath.equals(MODULE)){
                        //单独处理module.yaml
                        parseOtherConfig(projects.get(projectPath).getModules().get(modulePath),filePath);
                    } else {
                        if (apiPath != null && !apis.containsKey(apiPath)){
                            ParseApi parseApi = new ParseApi(filePath,projects.get(projectPath).getApiBaseModel());
                            apis.put(apiPath,parseApi.getApiModel());
                        }
                    }

                }
            }


        }
    }

    /**
     * 解析project.yaml,module.yaml……中的var,setup,teardown,api级和case级的var放在parseApi中解析
     * @param baseModel 当前节点对象
     * @param configPath 当前节点的路径
     */
    private void parseOtherConfig(BaseModel baseModel,String[] configPath) throws Exception {
        String filePath = FileKeyWords.CASEBASEPATH;
        for (String s : configPath){
            filePath = filePath + File.separator + s;
        }
        //组装var
        Map mapFromConfig = YamlUtil.read(filePath + ".yaml");
        if (mapFromConfig != null){
            if (mapFromConfig.containsKey(VAR)){
                if (mapFromConfig.get(VAR) != null){
                    baseModel.setVar((Map) mapFromConfig.get(VAR));
                }
            }
            //setup
            if (mapFromConfig.containsKey(SETUP)){
                if (mapFromConfig.get(SETUP) != null){
                    ParseFixture parseFixture = new ParseFixture();
                    baseModel.setSetup(parseFixture.getFixtureModel(mapFromConfig,SETUP));
                }
            }
            //teardown
            if (mapFromConfig.containsKey(TEARDOWN)){
                if (mapFromConfig.get(TEARDOWN) != null){
                    ParseFixture parseFixture = new ParseFixture();
                    baseModel.setSetup(parseFixture.getFixtureModel(mapFromConfig,TEARDOWN));
                }
            }
        }

    }
}
