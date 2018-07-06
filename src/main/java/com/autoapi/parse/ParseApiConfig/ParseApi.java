package com.autoapi.parse.ParseApiConfig;

import com.autoapi.model.CaseModel;
import com.autoapi.util.CommonUtil;
import com.autoapi.util.YamlUtil;

import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;

public class ParseApi {
    private Map<String,String> vars;                                  //变量
    private Map<String,CaseModel> cases;               //case
    private String[] casePath;                           //api文件地址
    public ParseApi(String[] casePath) {
        this.casePath = casePath;
    }

    private void parseCase(String casePath){
        Map map = YamlUtil.read(casePath);
        //解析var
        if (map.containsKey(VAR)){
             Map vars_temp= (Map) map.get(VAR);
             for (Object key : vars_temp.keySet()){
                 vars.put(CommonUtil.toStr(key),CommonUtil.toStr(vars_temp.get(key)));
             }
        }
        //解析test-case:params,headers,body,var:替换的变量
        if (map.containsKey(TESTCASE)){
            Map cases = (Map) map.get(TESTCASE);
            for(Object key : cases.keySet()){
            }

        }

    }
}
