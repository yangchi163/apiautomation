package com.autoapi.parse;

import com.autoapi.model.CaseModel;
import com.autoapi.model.RequestModel;
import com.autoapi.parse.ParseApiConfig.ParseFixture;
import com.autoapi.util.CommonUtil;

import java.util.HashMap;
import java.util.Map;

import static com.autoapi.keywords.RequestKeyWords.*;

public class ParseUtil {
    /**
     * 将测试用例中的数据，与api.yaml中的数据合并成测试请求
     * @param caseDetail  测试用例文件中的数据
     * @param requestModelFromBase api.yaml文件中对应的接口数据
     * @return
     */
    public static CaseModel getCaseModel(Map caseDetail, RequestModel requestModelFromBase) throws Exception {
        CaseModel caseModel = new CaseModel("");
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
            //设置case级var
            if (caseDetail.containsKey(VAR)){
                caseModel.setVar((Map) caseDetail.get(VAR));
            }
            //设置setup
            if (caseDetail.containsKey(SETUP)){
                ParseFixture parseFixture = new ParseFixture();
                parseFixture.getFixtureModel(caseDetail,SETUP);
            }
            //设置teardown
            if (caseDetail.containsKey(TEARDOWN)){
                ParseFixture parseFixture = new ParseFixture();
                parseFixture.getFixtureModel(caseDetail,TEARDOWN);
            }
            //设置output
            if (caseDetail.containsKey(OUTPUT)){
                caseModel.setOutput((String) caseDetail.get(OUTPUT));
            }
        }
        //将case中的信息和base中的信息合并
        Map headers = CommonUtil.mergeMap(requestModelFromBase.getHeaders(),headersFromCase);
        caseModel.getRequest().setHeaders(headers);
        Map params = CommonUtil.mergeMap(caseModel.getRequest().getUrlModel().getParams(),paramsFromCase);
        caseModel.getRequest().getUrlModel().setParams(params);
        Map body = CommonUtil.mergeMap(requestModelFromBase.getBody(),bodyFromCase);
        caseModel.getRequest().setBody(body);
        return caseModel;
    }
}
