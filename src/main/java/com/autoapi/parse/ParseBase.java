package com.autoapi.parse;

import com.autoapi.model.RequestBaseModel;
import com.autoapi.model.RequestModel;
import com.autoapi.model.UrlModel;
import com.autoapi.util.CommonUtil;
import com.autoapi.util.YamlUtil;

import java.util.HashMap;
import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;

/**
 * 解析api.yaml
 */
public class ParseBase {
    //最终返回的api信息，只有配置，没有具体参数，待组装
    private Map apis;

    private RequestBaseModel baseModel;
    private Map api ;
    private String filePath;

    public ParseBase(String filePath) {
        this.filePath = filePath;
        baseModel = new RequestBaseModel();
    }

    /**
     * 根据api.yaml生成api基本信息的模版
     * @return
     * @throws Exception
     */
    public Map getApis() throws Exception {
        parseBase();
        apis = new HashMap();
        //模块层
        for (Object moduleKey : api.keySet()){
            apis.put(moduleKey,new HashMap());
            //得到一组api
            Map apiDetails = (Map) api.get(moduleKey);
            //api层
            for (Object apiKey : apiDetails.keySet()){
                //单个api信息
                Map apiDetail = (Map) apiDetails.get(apiKey);
                //组装request
                RequestModel requestModel = new RequestModel();
                //处理path
                try {
                    UrlModel urlModel = new UrlModel();
                    urlModel.setSchema(baseModel.urlModel.getSchema());
                    urlModel.setHost(baseModel.urlModel.getHost());
                    urlModel.setPort(baseModel.urlModel.getPort());
                    urlModel.setVersion(baseModel.urlModel.getVersion());
                    urlModel.setPath(CommonUtil.toStr(apiDetail.get(PATH)));
                    requestModel.setUrlModel(urlModel);
                }catch (Exception e){
                    throw new Exception("api.yaml.api." + moduleKey + "." +apiKey + "缺少path");
                }
                //处理method
                if (apiDetail.containsKey(METHOD)){
                    requestModel.setMethod(CommonUtil.toStr(apiDetail.get(METHOD)));
                }else {
                    requestModel.setMethod(baseModel.getMethod());
                }
                //处理

            }
        }
        return apis;
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
            String schema = CommonUtil.toStr(url.get(SCHEMA));
            String host = CommonUtil.toStr(url.get(HOST));
            String port = CommonUtil.toStr(url.get(PORT));
            String version = CommonUtil.toStr(url.get(VERSION));
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
            baseModel.setMethod(CommonUtil.toStr(base.get(METHOD)));
        }
        //解析headers
        if (base.containsKey(HEADERS)){
            Map headers = (Map) base.get(HEADERS);
            baseModel.setHeaders(headers);
        }

    }


}
