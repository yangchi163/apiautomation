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
 * 解析用例文件：获取验证码.yaml
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
                FileKeyWords.CASEBASEPATH + File.separator + this.projectPath + File.separator + FileKeyWords.API + ".yaml";
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
             for (Object key : vars_temp.keySet()){
                 apiModel.getVar().put(CommonUtil.toStr(key),CommonUtil.toStr(vars_temp.get(key)));
             }
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
                //组装request.headers
                Map<String,String> headersFromCase = new HashMap<String, String>();
                if (caseDetail.containsKey(HEADERS)){
                    headersFromCase = (Map<String, String>) caseDetail.get(HEADERS);
                }
                Map headers = CommonUtil.mergeMap(requestModelFromBase.getHeaders(),headersFromCase);
                caseModel.getRequest().setHeaders(headers);
                //组装request.params
                Map paramsFromCase = new HashMap();
                if (caseDetail.containsKey(PARAMS)){
                    paramsFromCase = (Map) caseDetail.get(PARAMS);
                }
                Map params = CommonUtil.mergeMap(caseModel.getRequest().getUrlModel().getParams(),paramsFromCase);
                caseModel.getRequest().getUrlModel().setParams(params);
                //组装request.body
                Map bodyFromCase = new HashMap();
                if (caseDetail.containsKey(BODY)){
                    bodyFromCase = (Map) caseDetail.get(BODY);
                }
                Map body = CommonUtil.mergeMap(requestModelFromBase.getBody(),bodyFromCase);
                caseModel.getRequest().setBody(body);
                apiModel.getCases().put((String) caseKey,caseModel);
            }

        }

    }
}
