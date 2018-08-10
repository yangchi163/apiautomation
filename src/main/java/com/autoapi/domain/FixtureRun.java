package com.autoapi.domain;

import com.autoapi.model.*;

import java.io.InputStream;
import java.util.List;

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
            for (Object fixture: fixtureList){
                if (fixture instanceof SqlModel){
                    runUtil.runSql(inputStream, (SqlModel) fixture,baseModel);
                }
                if (fixture instanceof FunctionModel){
                    runUtil.runFunction((FunctionModel) fixture,baseModel);
                }
                if (fixture instanceof CaseModel){
                    runUtil.runApi((CaseModel) fixture,baseModel);
                }
            }

        }
    }

}
