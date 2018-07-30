package com.autoapi.domain;

import com.autoapi.httprequest.HttpClientUtil;
import com.autoapi.model.*;
import com.autoapi.model.http.HttpClientRequest;
import com.autoapi.model.http.HttpClientResponse;
import com.autoapi.parse.ParseApiConfig.ParseApiConfig;
import com.autoapi.parse.ParseApiConfig.ParseDirecotory;
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

    public void run(String projectName, String moduleName, String apiName,String caseName) throws Exception {
        ProjectModel projectModel =  this.apiConfig.getProjects().get(projectName);
        ModuleModel moduleModel = projectModel.getModules().get(moduleName);
        ApiModel apiModel = moduleModel.getApis().get(apiName);
        CaseModel caseModel = apiModel.getCases().get(caseName);
        //获取请求
        HttpClientRequest request = getRequest(caseModel);
        //执行请求,获得相应
        HttpClientUtil clientUtil = new HttpClientUtil();
        HttpClientResponse response = clientUtil.doRequest(request);
        System.out.println(response);
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
}
