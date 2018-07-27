package com.autoapi.parse.ParseApiConfig;

import com.autoapi.keywords.FileKeyWords;
import com.autoapi.model.*;
import com.autoapi.parse.ParseBase;
import com.autoapi.util.CommonUtil;
import com.autoapi.util.YamlUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;

/**
 * 解析用例文件：获取验证码.yaml,project.yaml,module.yaml
 * 返回apimodel
 */
public class ParseApi {
    private ApiModel apiModel;
    private String projectPath;
    private String modulePath;
    private String apiPath;
    private ProjectModel apiBaseModel;

    public ParseApi(String[] casePath) {
        this(casePath[0],casePath[1],casePath[2]);
    }

    public ParseApi(String projectPath,String modulePath,String apiPath){
        this.projectPath = projectPath;
        this.modulePath = modulePath;
        this.apiPath = apiPath;
        apiModel = new ApiModel(this.apiPath);
    }

    private void setApiBaseModel() throws Exception {
        String apiConfigPath =
                FileKeyWords.CASEBASEPATH + File.separator + this.projectPath + File.separator + FileKeyWords.APIFILE + ".yaml";
        apiBaseModel = new ParseBase(apiConfigPath).getApiBaseModel();
    }

    public ApiModel getApiModel() throws Exception {
        String apiFilePath =
                FileKeyWords.CASEBASEPATH + File.separator + this.projectPath + File.separator + this.modulePath
                + File.separator + this.apiPath + ".yaml";
        parseCase(apiFilePath);
        return apiModel;
    }

    private void parseCase(String apiPath) throws Exception {
        setApiBaseModel();
        Map map = YamlUtil.read(apiPath);
        //解析var
        if (map.containsKey(VAR)){
             Map vars_temp= (Map) map.get(VAR);
                 apiModel.setVar(vars_temp);
        }
        //解析test-case:params,headers,body,var:替换的变量
        if (map.containsKey(TESTCASE)){
            //cases是所有用例
            Map cases = (Map) map.get(TESTCASE);
            for(Object caseKey : cases.keySet()){
                Map caseDetail = (Map) cases.get(caseKey);
                CaseModel caseModel = new CaseModel((String) caseKey);
                RequestModel requestModelFromBase =  apiBaseModel.getModules().get(this.modulePath).getApis().get(this.apiPath).getRequestModel();
                //组装request.urlmodel
                caseModel.getRequest().setUrlModel(requestModelFromBase.getUrlModel());
                //组装request.method
                caseModel.getRequest().setMethod(requestModelFromBase.getMethod());
                //组装request.headers,params,body
                Map<String,String> headersFromCase = new HashMap<String, String>();
                Map paramsFromCase = new HashMap();
                Map bodyFromCase = new HashMap();
                if (caseDetail != null){
                    //获取case中的信息
                    if (caseDetail.containsKey(HEADERS)){
                        headersFromCase = (Map<String, String>) caseDetail.get(HEADERS);
                    }
                    if (caseDetail.containsKey(PARAMS)){
                        paramsFromCase = (Map) caseDetail.get(PARAMS);
                    }
                    if (caseDetail.containsKey(BODY)){
                        bodyFromCase = (Map) caseDetail.get(BODY);
                    }
                    //设置var
                    if (caseDetail.containsKey(VAR)){
                        caseModel.setVar((Map) caseDetail.get(VAR));
                    }
                    //设置setup
                    if (caseDetail.containsKey(SETUP)){

                    }
                    //设置teardown
                    if (caseDetail.containsKey(TEARDOWN)){

                    }
                }
                //将case中的信息和base中的信息合并
                Map headers = CommonUtil.mergeMap(requestModelFromBase.getHeaders(),headersFromCase);
                caseModel.getRequest().setHeaders(headers);
                Map params = CommonUtil.mergeMap(caseModel.getRequest().getUrlModel().getParams(),paramsFromCase);
                caseModel.getRequest().getUrlModel().setParams(params);
                Map body = CommonUtil.mergeMap(requestModelFromBase.getBody(),bodyFromCase);
                caseModel.getRequest().setBody(body);
                //casemodel组装好，并放入apimodel下
                apiModel.getCases().put((String) caseKey,caseModel);
            }

        }

    }
}
