package com.autoapi.domain;

import com.autoapi.httprequest.HttpClientUtil;
import com.autoapi.model.*;
import com.autoapi.model.http.HttpClientRequest;
import com.autoapi.model.http.HttpClientResponse;
import com.autoapi.parse.ParseApiConfig.ParseApiConfig;
import com.autoapi.parse.ParseApiConfig.ParseDirecotory;
import com.autoapi.util.CommonUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiRun {
    private ApiConfig apiConfig;

    public ApiRun() throws Exception {
        ParseDirecotory parseDirecotory = new ParseDirecotory();
        ParseApiConfig apiConfig = new ParseApiConfig(parseDirecotory.getCasePath());
        this.apiConfig = apiConfig.getApiConfig();

    }

    /**
     *真正执行用例的方法
     * @throws Exception
     */
    public void doRun() throws Exception {
        Map<String,ProjectModel> projectModelMap = this.apiConfig.getProjects();
        //project层
        for (Object keyProject : projectModelMap.keySet()){
            ProjectModel projectModel = projectModelMap.get(keyProject);
            if (projectModel.isRun()){
                //执行setup
                //FixtureModel projectSetup = projectModel.getSetup();
                Map<String,ModuleModel> moduleModelMap = projectModel.getModules();
                for (Object keyModule : moduleModelMap.keySet()){
                    //module层
                    ModuleModel moduleModel = moduleModelMap.get(keyModule);
                    if (moduleModel.isRun()){
                        //执行变量替换

                        //执行setup
                        //FixtureModel moduleSetup = moduleModel.getSetup();
                        Map<String,ApiModel> apiModelMap = moduleModel.getApis();
                        for (Object keyApi : apiModelMap.keySet()){
                            //api层
                            ApiModel apiModel = apiModelMap.get(keyApi);
                            if (apiModel.isRun()){
                                //执行setup
                                //FixtureModel apiSetup = apiModel.getSetup();
                                Map<String,CaseModel> caseModelMap = apiModel.getCases();
                                for (Object keyCase:caseModelMap.keySet()){
                                    //case层
                                    CaseModel caseModel = caseModelMap.get(keyCase);
                                    if (caseModel.isRun()){
                                        //执行setup
                                        //FixtureModel caseSetup = caseModel.getSetup();

                                        //获取请求
                                        HttpClientRequest request = getRequest(caseModel);
                                        //执行请求,获得相应
                                        HttpClientUtil clientUtil = new HttpClientUtil();
                                        HttpClientResponse response = clientUtil.doRequest(request);
                                        System.out.println(response);

                                        //执行teardown
                                        //FixtureModel caseTeardown = caseModel.getTeardown();

                                        //执行完，给父节点执行计数+1
                                        //apiModel.setSonHasRunNumber();
                                    }

                                }
                                //执行teardown
                                //FixtureModel apiTeardown = apiModel.getTeardown();
                                //执行完，给父节点执行计数+1
                                //moduleModel.setSonHasRunNumber();
                            }

                        }
                        //执行tearDown
                        //FixtureModel moduleTeardown = moduleModel.getTeardown();
                        //执行完，给父节点执行计数+1
                        //projectModel.setSonHasRunNumber();
                    }

                }
                //执行teardown
                //FixtureModel projectTeardown = projectModel.getTeardown();
            }
        }

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
     * 将casemodel转化成HttpClientRequest，此时变量已替换完成
     * @param caseModel
     * @return
     */
    private HttpClientRequest getRequest(CaseModel caseModel) throws URISyntaxException {
        HttpClientRequest request = new HttpClientRequest();
        String url = getUrl(caseModel.getRequest().getUrlModel());
        request.setMethod(caseModel.getRequest().getMethod());
        request.setUrl(url);
        request.setHeaders(caseModel.getRequest().getHeaders());
        request.setBody(caseModel.getRequest().getBody());
        return request;
    }

    /**
     * 将urlmodel解析成url
     * @param urlModel
     * @return
     * @throws URISyntaxException
     */
    private String getUrl(UrlModel urlModel) throws URISyntaxException {
        //拼接url
        String schema = urlModel.getSchema();
        String host = urlModel.getHost();
        int port = urlModel.getPort();
        String version = urlModel.getVersion();
        String path = urlModel.getPath();
        if (version != null){
            path = version + "/" + path;
        }
        URIBuilder uriBuilder = new URIBuilder().setScheme(schema).setHost(host).setPort(port)
                .setPath(path);
        //组装params
        Map params = urlModel.getParams();
        if (params != null){
            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            for (Object k : params.keySet()){
                NameValuePair pair = new BasicNameValuePair((String) k,(String) params.get(k));
                pairList.add(pair);
            }
            uriBuilder.setParameters(pairList);
        }

        String url = uriBuilder.build().toString();
        return url;
    }

    /**
     * 变量替换
     * @param o 待替换的变量
     * @param varPath 当前对象在apiconfig中的路径
     */
    private void replaceVar(Object o,String[] varPath){
        if (o instanceof BaseModel){

        }
    }

    /**
     * 只考虑了map,基本值:暂时没有考虑list
     * @param map
     * @param varPath
     */
    private void replcevar(Map map,String[] varPath){
        for (Object keyMap : map.keySet()){
            //替换map中的值
            if (map.get(keyMap) instanceof String){
                String value = (String) map.get(keyMap);
                if (CommonUtil.getFirstString(value) != null){
                    map.put(keyMap,getReplaceValue(varPath));
                }
            }
            if (map.get(keyMap) instanceof Map){
                replaceVar(map.get(keyMap),varPath);
            }
        }
    }

    private void replacevar(FixtureModel fixtureModel, String[] varPath){

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
