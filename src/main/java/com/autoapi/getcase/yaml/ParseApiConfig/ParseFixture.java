package com.autoapi.getcase.yaml.ParseApiConfig;

import com.autoapi.keywords.FileKeyWords;
import com.autoapi.model.*;
import com.autoapi.getcase.yaml.ParseBase;
import com.autoapi.getcase.yaml.ParseUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.autoapi.keywords.RequestKeyWords.*;
import static com.autoapi.keywords.RequestKeyWords.API;


public class ParseFixture {
    private FixtureModel fixtureModel;

    public ParseFixture() {
        this.fixtureModel = new FixtureModel();
    }

    public FixtureModel getFixtureModel(Map mapFromConfig, String key) throws Exception {
        fixtureModel.setFixture(getFixture(mapFromConfig, key));
        return fixtureModel;
    }

    /**
     *
     * @param mapFromConfig  由yaml文件解析出来的map
     * @param key  setup,teardown等key
     */
    private List getFixture(Map mapFromConfig, String key) throws Exception {
        //res : 用来最终容纳fixture
        List res = new ArrayList();
        if (mapFromConfig.containsKey(key)){
            //listFromConfig: 整个setup节点对应的值
            List listFromConfig = (List) mapFromConfig.get(key);
            if (listFromConfig != null){
                //o,m: 一个sql执行对象，或api对象（包含key），或function对象
                for (Object o : listFromConfig){
                    Map m = (Map) o;
                    if (m.containsKey(SQL)){
                        Map sqlMapFromConfig = (Map) m.get(SQL);
                        SqlModel sqlModel = ParseUtil.getSqlModel(sqlMapFromConfig);
                        res.add(sqlModel);
                    } else if (m.containsKey(API)){
                        Map apiMapFromConfig = (Map) m.get(API);
                        CaseModel caseModel = null;
                        for (Object apiName : apiMapFromConfig.keySet()) {
                            String apiNames = (String) apiName;
                            //setup,teardown中的api必须是全路径：project.module.apiname
                            String[] apiPath = apiNames.split("\\.");
                            Map caseDetail = (Map) apiMapFromConfig.get(apiName);
                            //获取base中的api信息
                            String apiConfigPath = FileKeyWords.CASEBASEPATH + File.separator + apiPath[0] + File.separator + FileKeyWords.APIFILE + ".yaml";
                            ParseBase parseBase = new ParseBase(apiConfigPath);
                            RequestModel requestModelFromBase = parseBase.getApiBaseModel().getModules().get(apiPath[1]).getApis().get(apiPath[2]).getRequestModel();
                            caseModel = ParseUtil.getCaseModel(caseDetail, requestModelFromBase);
                            caseModel.setName(apiPath[2]);
                        }
                        res.add(caseModel);
                    } else if(m.containsKey(FUNCTION)){
                        Map functionMapFromConfig = (Map) m.get(FUNCTION);
                        FunctionModel functionModel = ParseUtil.getFunctionModel(functionMapFromConfig);
                        res.add(functionModel);
                    } else {
                        //不存在的场景
                    }
                }
            }

        }
        return res;
    }

}
