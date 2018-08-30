package com.autoapi.parse.ParseApiConfig;

import com.autoapi.keywords.FileKeyWords;
import com.autoapi.model.*;
import com.autoapi.parse.ParseBase;
import com.autoapi.parse.ParseUtil;
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
        this(casePath[0],casePath[1],casePath[2],apiBaseModel);
    }

    public ParseApi(String projectPath,String modulePath,String apiPath,ProjectModel apiBaseModel){
        this.projectPath = projectPath;
        this.modulePath = modulePath;
        this.apiPath = apiPath;
        apiModel = new ApiModel(this.apiPath);
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
