package com.autoapi.domain;

import com.autoapi.model.BaseModel;
import com.autoapi.model.CaseModel;
import com.autoapi.model.FixtureModel;
import com.autoapi.model.SqlModel;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import static com.autoapi.keywords.RequestKeyWords.*;

public class FixtureRun {

    public FixtureRun() {
    }


    /**
     * 执行fixturemodel：fixture中的api不执行fixture部分，如有需要，可以增加执行
     * @param fixtureModel
     * @param baseModel fixture所处的节点
     * @throws Exception
     */
    public void runFixture(FixtureModel fixtureModel,BaseModel baseModel,InputStream inputStream) throws Exception {
        if (fixtureModel != null){
            RunUtil runUtil = new RunUtil();
            List fixtureList = fixtureModel.getFixture();
            for (Object fixture:fixtureList){
                //fixture是map
                Map fixtureMap = (Map) fixture;
                if (fixtureMap.containsKey(SQL)){
                    List sqlModelList = (List) fixtureMap.get(SQL);
                    for (Object o:sqlModelList){
                        SqlModel sqlModel = (SqlModel) o;
                        runUtil.runSql(inputStream,sqlModel,baseModel);
                    }
                }
                if (fixtureMap.containsKey(API)){
                    List caseModelList = (List) fixtureMap.get(API);
                    for (Object o : caseModelList){
                        CaseModel caseModel = (CaseModel) o;
                        runUtil.runApi(caseModel,baseModel);
                    }
                }
            }
        }
    }

}
