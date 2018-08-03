package com.autoapi.parse.ParseApiConfig;

import com.autoapi.keywords.FileKeyWords;
import com.autoapi.model.CaseModel;
import com.autoapi.model.FixtureModel;
import com.autoapi.model.RequestModel;
import com.autoapi.model.SqlModel;
import com.autoapi.parse.ParseBase;
import com.autoapi.parse.ParseUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
                //o,m: 一个sql执行对象，或api对象（包含key）
                for (Object o : listFromConfig){
                    Map m = (Map) o;
                    if (m.containsKey(SQL)){
                        Map mapRes = new HashMap();
                        List sqlResList = new ArrayList();
                        //sqlListFromConfig: sql节点下所有的sql集合
                        List sqlListFromConfig = (List) m.get(SQL);
                        for (Object sqlDedailFromConfig : sqlListFromConfig){
                            //sqlDedailFromConfig,mapSqlFromConfig:单个sql的具体执行信息
                            Map mapSqlFromConfig = (Map) sqlDedailFromConfig;
                            SqlModel sqlModel = new SqlModel();
                            if (mapSqlFromConfig.containsKey(SQL)){
                                sqlModel.setSql((String) mapSqlFromConfig.get(SQL));
                            }
                            if (mapFromConfig.containsKey(CONN)){
                                sqlModel.setConn((String) mapFromConfig.get(CONN));
                            }
                            if (mapFromConfig.containsKey(PARAMS)){
                                if (mapFromConfig.get(PARAMS) != null){
                                    sqlModel.setParams((Map) mapFromConfig.get(PARAMS));
                                }
                            }
                            if (mapFromConfig.containsKey(OUTPUT)){
                                sqlModel.setOutput((String) mapFromConfig.get(OUTPUT));
                            }
                            sqlResList.add(sqlModel);
                        }
                        mapRes.put(SQL,sqlResList);
                        res.add(mapRes);
                    } else if (m.containsKey(API)){
                        Map mapRes = new HashMap();
                        List apiResList = new ArrayList();
                        //apiListFromConfig: api节点下所有的api集合
                        List apiListFromConfig = (List) m.get(API);
                        for (Object apiDedailFromConfig : apiListFromConfig){
                            //apiDedailFromConfig,mapApiFromConfig:单个api的具体执行信息
                            Map mapApiFromConfig = (Map) apiDedailFromConfig;
                            for (Object apiName : mapApiFromConfig.keySet()){
                                String apiNames = (String) apiName;
                                //setup,teardown中的api必须是全路径：project.module.apiname
                                String[] apiPath = apiNames.split("\\.");
                                Map caseDetail = (Map) mapApiFromConfig.get(apiName);
                                //获取base中的api信息
                                String apiConfigPath = FileKeyWords.CASEBASEPATH + File.separator + apiPath[0] + File.separator + FileKeyWords.APIFILE + ".yaml";
                                ParseBase parseBase = new ParseBase(apiConfigPath);
                                RequestModel requestModelFromBase = parseBase.getApiBaseModel().getModules().get(apiPath[1]).getApis().get(apiPath[2]).getRequestModel();
                                CaseModel caseModel = ParseUtil.getCaseModel(caseDetail,requestModelFromBase);
                                caseModel.setName(apiPath[2]);
                                apiResList.add(caseModel);
                            }
                        }
                        mapRes.put(API,apiResList);
                        res.add(mapRes);
                    } else {
                        //不存在的场景
                    }
                }
            }

        }
        return res;
    }

}
