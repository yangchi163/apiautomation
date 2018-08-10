package com.autoapi.parse;

import com.autoapi.model.*;
import com.autoapi.util.CommonUtil;
import com.autoapi.util.YamlUtil;

import java.util.HashMap;
import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;

/**
 * 解析api.yaml
 */
public class ParseBase {
    private RequestModel baseModel = new RequestModel("");
    private Map api ;
    private String filePath;
    private ProjectModel projectBaseModel = new ProjectModel();

    public ParseBase(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 根据api.yaml生成api基本信息的模版
     * @return
     * @throws Exception
     */
    public ProjectModel getApiBaseModel() throws Exception {
        parseBase();
        //模块层
        if (api != null){
            for (Object moduleKey : api.keySet()){
                //解析模块
                ModuleModel moduleModel = new ModuleModel((String) moduleKey);
                //得到一组api
                Map apiDetails = (Map) api.get(moduleKey);

                if (apiDetails != null){
                    //api层
                    for (Object apiKey : apiDetails.keySet()){
                        //单个api信息
                        Map apiDetail = (Map) apiDetails.get(apiKey);
                        //组装request
                        ApiModel apiModel = new ApiModel((String) apiKey);
                        RequestModel requestModel = new RequestModel((String) apiKey);
                        //处理path，一定要有
                        try {
                            UrlModel urlModel = new UrlModel();
                            urlModel.setSchema(baseModel.getUrlModel().getSchema());
                            urlModel.setHost(baseModel.getUrlModel().getHost());
                            urlModel.setPort(baseModel.getUrlModel().getPort());
                            urlModel.setVersion(baseModel.getUrlModel().getVersion());
                            urlModel.setPath((String) apiDetail.get(PATH));
                            requestModel.setUrlModel(urlModel);
                        }catch (Exception e){
                            throw new Exception("api.yaml.api." + moduleKey + "." +apiKey + "缺少path");
                        }
                        //处理method
                        if (apiDetail.containsKey(METHOD)){
                            requestModel.setMethod((String) apiDetail.get(METHOD));
                        }else {
                            requestModel.setMethod(baseModel.getMethod());
                        }
                        //处理params
                        if (apiDetail.containsKey(PARAMS)){
                            Map params = (Map) apiDetail.get(PARAMS);
                            requestModel.getUrlModel().setParams(params);
                        }
                        //处理headers
                        Map copy = new HashMap();
                        Map headers = new HashMap();
                        if (baseModel.getHeaders() != null){
                            copy.putAll(baseModel.getHeaders());
                        }
                        if (apiDetail.containsKey(HEADERS)){
                            headers = (Map) apiDetail.get(HEADERS);
                        }
                        requestModel.setHeaders(CommonUtil.mergeMap(copy,headers));
                        //处理body
                        if (apiDetail.containsKey(BODY)){
                            Map body = (Map) apiDetail.get(BODY);
                            requestModel.setBody(body);
                        }
                        apiModel.setRequestModel(requestModel);
                        moduleModel.getApis().put(apiModel.getName(),apiModel);
                    }
                }
                projectBaseModel.getModules().put(moduleModel.getName(),moduleModel);
            }
        }
        return projectBaseModel;
    }


    /**
     * 主要将api.yaml.base解析到basemodel
     * url中没有path
     */
    private void parseBase() throws Exception {
        Map obj = YamlUtil.read(filePath);
        Map base = (Map) obj.get(BASE);
        api = (Map) obj.get(API);

        //解析url
        UrlModel urlModel = new UrlModel();
        try{
            Map url = (Map) base.get(URL);
            String schema = (String) url.get(SCHEMA);
            String host = (String) url.get(HOST);
            Integer port = (Integer) url.get(PORT);
            String version = (String) url.get(VERSION);
            urlModel.setSchema(schema);
            urlModel.setHost(host);
            urlModel.setPort(port);
            urlModel.setVersion(version);
            baseModel.setUrlModel(urlModel);
            //System.out.println(urlModel);
        }catch (Exception e){
            throw new Exception("api.yaml.base.url配置异常");
        }
        //解析method
        if (base.containsKey(METHOD)){
            baseModel.setMethod((String) base.get(METHOD));
        }
        //解析headers
        if (base.containsKey(HEADERS)){
            Map headers = (Map) base.get(HEADERS);
            baseModel.setHeaders(headers);
        }

    }


}
