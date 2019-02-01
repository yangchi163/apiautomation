package com.autoapi.getcase.yaml.ParseApiConfig;

import com.autoapi.getcase.yaml.ParseUtil;
import com.autoapi.keywords.FileKeyWords;
import com.autoapi.model.*;
import com.autoapi.util.YamlUtil;

import java.io.File;
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

    public ParseApi(String[] casePath,ProjectModel apiBaseModel) {
        projectPath = casePath[0];
        modulePath = casePath[1];
        apiPath = casePath[2];
        //s代表的是包名
        String[] s = {projectPath,modulePath};
        apiModel = new ApiModel(apiPath,s);
        this.apiBaseModel = apiBaseModel;
    }


    public ApiModel getApiModel() throws Exception {
        String apiFilePath =
                FileKeyWords.CASEBASEPATH + File.separator + this.projectPath + File.separator + this.modulePath
                + File.separator + this.apiPath + ".yaml";
        parseCase(apiFilePath);
        return apiModel;
    }

    /**
     *
     * @param apiPath api文件全路径
     * @throws Exception
     */
    private void parseCase(String apiPath) throws Exception {
        Map map = YamlUtil.read(apiPath);
        //解析api级var
        if (map.containsKey(VAR)){
            if (map.get(VAR) != null){
                Map vars_temp= (Map) map.get(VAR);
                apiModel.setVar(vars_temp);
            }
        }
        //解析test-case:params,headers,body,var:替换的变量
        if (map.containsKey(TESTCASE)){
            //cases是所有用例
            Map cases = (Map) map.get(TESTCASE);
            for(Object caseKey : cases.keySet()){
                Map caseDetail = (Map) cases.get(caseKey);
                RequestModel requestModelFromBase =  apiBaseModel.getModules().get(this.modulePath).getApis().get(this.apiPath).getRequestModel();
                CaseModel caseModel = ParseUtil.getCaseModel(caseDetail,requestModelFromBase);
                caseModel.setName((String) caseKey);
                apiModel.getCases().put((String) caseKey,caseModel);
            }
        }

    }
}
